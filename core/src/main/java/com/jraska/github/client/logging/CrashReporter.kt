package com.jraska.github.client.logging

interface CrashReporter {
  fun report(error: Throwable, vararg messages: String): CrashReporter
}
