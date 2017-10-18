package com.jraska.github.client.logging

import com.google.firebase.crash.FirebaseCrash

internal open class FirebaseCrashProxy {
  open fun log(message: String) {
    FirebaseCrash.log(message)
  }

  open fun report(error: Throwable?) {
    FirebaseCrash.report(error)
  }
}
