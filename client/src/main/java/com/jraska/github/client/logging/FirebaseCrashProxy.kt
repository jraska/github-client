package com.jraska.github.client.logging

import com.google.firebase.crash.FirebaseCrash

internal open class FirebaseCrashProxy {
  fun log(message: String) {
    FirebaseCrash.log(message)
  }

  fun report(error: Throwable) {
    FirebaseCrash.report(error)
  }
}
