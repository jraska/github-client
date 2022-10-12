package com.jraska.github.client.core.android.firebase

import com.jraska.github.client.logging.CrashReporter

class FirebaseCrashlyticsReporter
internal constructor(private val crashlyticsProxy: FirebaseCrashlyticsProxy) : CrashReporter {
  constructor() : this(FirebaseCrashlyticsProxy())

  override fun report(error: Throwable, vararg messages: String): CrashReporter {
    for (message in messages) {
      crashlyticsProxy.log(message)
    }

    crashlyticsProxy.report(error)
    return this
  }
}
