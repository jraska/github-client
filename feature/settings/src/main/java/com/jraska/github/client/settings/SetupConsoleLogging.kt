package com.jraska.github.client.settings

import android.app.Application
import android.util.Log
import com.jraska.console.timber.ConsoleTree
import com.jraska.github.client.core.android.OnAppCreate
import timber.log.Timber

internal class SetupConsoleLogging : OnAppCreate {
  override fun onCreate(app: Application) {
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
