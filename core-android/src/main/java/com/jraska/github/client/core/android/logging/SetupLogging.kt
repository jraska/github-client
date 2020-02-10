package com.jraska.github.client.core.android.logging

import android.app.Application
import com.jraska.github.client.core.android.BuildConfig
import com.jraska.github.client.core.android.OnAppCreate
import javax.inject.Inject
import javax.inject.Provider
import timber.log.Timber

class SetupLogging @Inject constructor(
  private val errorTree: ErrorReportTree,
  private val analyticsTree: Provider<AnalyticsLoggingTree>
) : OnAppCreate {
  override fun onCreate(app: Application) {
    Timber.plant(errorTree)
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
      Timber.plant(analyticsTree.get())
    }
  }
}
