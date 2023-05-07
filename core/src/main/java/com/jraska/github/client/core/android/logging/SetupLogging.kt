package com.jraska.github.client.core.android.logging

import android.app.Application
import com.jraska.github.client.core.BuildConfig
import com.jraska.github.client.core.android.OnAppCreate
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class SetupLogging @Inject constructor(
  private val errorTree: ErrorReportTree,
) : OnAppCreate {
  override fun onCreate(app: Application) {
    Timber.plant(errorTree)
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}
