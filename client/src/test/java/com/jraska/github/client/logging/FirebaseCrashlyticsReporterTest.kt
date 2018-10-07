package com.jraska.github.client.logging

import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.only
import org.mockito.Mockito.verify

class FirebaseCrashlyticsReporterTest {
  @Test
  fun whenNoMessageArgument_thenPasses() {
    val crashProxyMock = mock(FirebaseCrashlyticsProxy::class.java)
    val crashReporter = FirebaseCrashlyticsReporter(crashProxyMock)
    val exception = RuntimeException()

    crashReporter.report(exception)

    verify(crashProxyMock, only()).report(eq(exception))
  }
}
