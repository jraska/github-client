package com.jraska.github.client.identity.internal

import com.jraska.github.client.Fakes
import com.jraska.github.client.TestTimeProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class SessionIdProviderTest {
  private lateinit var timeProvider: TestTimeProvider
  private lateinit var sessionIdProvider: SessionIdProvider

  @Before
  fun setUp() {
    timeProvider = Fakes.testTimeProvider()
    sessionIdProvider = SessionIdProvider(timeProvider)
  }

  @Test
  fun returnsSameSessionIdAsTimeGoes() {
    val sessionId = sessionIdProvider.sessionId()

    for (time in 0 until (SessionIdProvider.SESSION_THRESHOLD_MS) step 1000) {
      timeProvider.advanceTime(time)
      assertThat(sessionIdProvider.sessionId()).isEqualTo(sessionId)
    }
  }

  @Test
  fun afterThresholdsGeneratesNewAndHolds() {
    val sessionId = sessionIdProvider.sessionId()

    timeProvider.advanceTime(SessionIdProvider.SESSION_THRESHOLD_MS + 1)

    val newSessionId = sessionIdProvider.sessionId()

    assertThat(newSessionId).isNotEqualTo(sessionId)
    for (time in 0 until (SessionIdProvider.SESSION_THRESHOLD_MS) step 1000) {
      timeProvider.advanceTime(time)
      assertThat(sessionIdProvider.sessionId()).isEqualTo(newSessionId)
    }
  }
}
