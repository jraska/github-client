package com.jraska.github.client

import android.app.Application
import com.jraska.github.client.core.android.logging.AnalyticsLoggingTree
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.core.android.logging.ErrorReportTree
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

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
