package com.jraska.github.client

import com.jraska.github.client.logging.CrashReporter

object EmptyCrashReporter : CrashReporter {
  override fun report(error: Throwable, vararg messages: String): CrashReporter {
    return this
  }
}
