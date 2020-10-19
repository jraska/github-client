package com.jraska.github.client.settings

import android.app.Application
import android.util.Log
import com.jraska.console.timber.ConsoleTree
import com.jraska.github.client.core.android.OnAppCreate
import timber.log.Timber
import javax.inject.Inject

internal class SetupConsoleLogging @Inject constructor() : OnAppCreate {
  override fun onCreate(app: Application) {
    if (BuildConfig.DEBUG) {
      Timber.plant(ConsoleTree.create())
    } else {
      ConsoleTree.builder()
        .minPriority(Log.DEBUG)
        .build()
        .also { Timber.plant(it) }
    }
  }
}
