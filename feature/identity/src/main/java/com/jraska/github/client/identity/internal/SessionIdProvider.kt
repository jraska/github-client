package com.jraska.github.client.identity.internal

import com.jraska.github.client.identity.SessionId
import com.jraska.github.client.time.TimeProvider
import org.threeten.bp.Duration

internal class SessionIdProvider(private val timeProvider: TimeProvider) {
  private var lastQueriedTime = Long.MIN_VALUE
  private var sessionId = SessionId.newSessionId()
  private val lock = Any()

  fun sessionId(): SessionId {
    val elapsed = timeProvider.elapsed()

    synchronized(lock) {
      val inactivityTime = elapsed - lastQueriedTime
      if (inactivityTime > SESSION_THRESHOLD_MS) {
        sessionId = SessionId.newSessionId()
      }
      lastQueriedTime = elapsed
    }

    return sessionId
  }

  companion object {
    internal val SESSION_THRESHOLD_MS = Duration.ofMinutes(15).toMillis()
  }
}
