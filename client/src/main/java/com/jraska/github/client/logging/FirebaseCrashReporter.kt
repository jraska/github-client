package com.jraska.github.client.logging

class FirebaseCrashReporter internal constructor(private val crashProxy: FirebaseCrashProxy) : CrashReporter {
  constructor() : this(FirebaseCrashProxy()) {}

  override fun report(error: Throwable, vararg messages: String): CrashReporter {
    for (message in messages) {
      crashProxy.log(message)
    }

    crashProxy.report(error)
    return this
  }
}
