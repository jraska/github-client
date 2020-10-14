package com.jraska.github.client.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics

internal open class FirebaseCrashlyticsProxy {
  open fun log(message: String) {
    FirebaseCrashlytics.getInstance().log(message)
  }

  open fun report(error: Throwable) {
    FirebaseCrashlytics.getInstance().recordException(error)
  }
}
