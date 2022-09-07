package com.jraska.github.client.performance.startup

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.jraska.github.client.core.android.DefaultActivityCallbacks
import com.jraska.github.client.core.android.OnAppCreate
import javax.inject.Inject

class StartupTimeMetric(
  private val reporter: StartupAnalyticsReporter
) {

  private var hadSavedState = false
  private var firstActivityCreated: String? = null

  private fun onAppCreate(app: Application) {
    if (isForegroundProcess()) {
      app.registerActivityLifecycleCallbacks(object : DefaultActivityCallbacks() {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
          if (firstActivityCreated == null) {
            firstActivityCreated = activity::class.simpleName
            hadSavedState = savedInstanceState != null
            activity.application.unregisterActivityLifecycleCallbacks(this)
          }
        }
      })

      Handler(Looper.getMainLooper()).post {
        if (firstActivityCreated != null) {
          reporter.reportForegroundLaunch(firstActivityCreated!!, hadSavedState)
        } else {
          reporter.reportForegroundLaunchWithoutActivity()
        }
      }
    } else {
      reporter.reportBackgroundLaunch()
    }
  }

  private fun isForegroundProcess(): Boolean {
    val processInfo = ActivityManager.RunningAppProcessInfo()
    ActivityManager.getMyMemoryState(processInfo)
    return processInfo.importance == IMPORTANCE_FOREGROUND
  }

  internal class RegisterStartupCallbacks @Inject constructor(private val startupTimeMetric: StartupTimeMetric) : OnAppCreate {
    override fun onCreate(app: Application) {
      startupTimeMetric.onAppCreate(app)
    }
  }
}
