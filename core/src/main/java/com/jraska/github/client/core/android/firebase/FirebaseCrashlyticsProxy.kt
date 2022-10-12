package com.jraska.github.client.core.android.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics

internal open class FirebaseCrashlyticsProxy {
  open fun log(message: String) {
    FirebaseCrashlytics.getInstance().log(message)
  }

  open fun report(error: Throwable) {
    FirebaseCrashlytics.getInstance().recordException(error)
  }
}
