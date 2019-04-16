package com.jraska.github.client.settings

import android.app.Application
import com.jraska.console.timber.ConsoleTree
import com.jraska.github.client.core.android.OnAppCreate
import timber.log.Timber

class SetupConsoleLogging : OnAppCreate {
  override fun onCreate(app: Application) {
    if (BuildConfig.DEBUG) {
      Timber.plant(ConsoleTree.create())
    }
  }
}
