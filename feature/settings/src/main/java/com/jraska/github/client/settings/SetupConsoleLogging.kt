package com.jraska.github.client.settings

import android.util.Log
import com.jraska.console.timber.ConsoleTree
import timber.log.Timber

internal class SetupConsoleLogging {
  fun onCreate() {
    if (BuildConfig.DEBUG) {
      Timber.plant(ConsoleTree.create())
    } else {
      ConsoleTree.builder()
        .minPriority(Log.INFO)
        .build()
        .also { Timber.plant(it) }
    }
  }
}
