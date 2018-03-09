package com.jraska.github.client.logging

import com.crashlytics.android.Crashlytics

internal open class FirebaseCrashlyticsProxy {
  open fun log(message: String) {
    Crashlytics.log(message)
  }

  open fun report(error: Throwable?) {
    Crashlytics.logException(error)
  }
}
