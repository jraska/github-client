package com.jraska.github.client.logging

import org.junit.Test

import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*

class FirebaseCrashReporterTest {
    @Test
    fun whenNoMessageArgument_thenPasses() {
        val crashProxyMock = mock(FirebaseCrashProxy::class.java)
        val crashReporter = FirebaseCrashReporter(crashProxyMock)
        val exception = RuntimeException()

        crashReporter.report(exception)

        verify(crashProxyMock, only()).report(eq(exception))
    }
}
