package com.jraska.github.client.performance.jank

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.metrics.performance.FrameData
import androidx.metrics.performance.JankStats
import com.jraska.github.client.core.android.DefaultActivityCallbacks
import com.jraska.github.client.core.android.OnAppCreate
import javax.inject.Inject

internal class JankMetric @Inject constructor(
  private val jankAnalyticsReporter: JankAnalyticsReporter
) : DefaultActivityCallbacks() {
  private val onFrameListener = JankStats.OnFrameListener { onFrame(it) }

  private fun onAppCreate(app: Application) {
    app.registerActivityLifecycleCallbacks(object : DefaultActivityCallbacks() {
      private var creatingActivity: Activity? =
        null // store this as we cannot call `createAndTrack` in onActivityCreated and `onActivityStarted` will be called multiple times

      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        creatingActivity = activity
      }

      override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)

        if (creatingActivity == activity) {
          JankStats.createAndTrack(activity.window, onFrameListener)
        }
        creatingActivity = null
      }
    })
  }

  private fun onFrame(frameData: FrameData) {
    if (frameData.isJank) {
      jankAnalyticsReporter.reportJank(frameData)
    }
  }

  internal class StartupOnCreate @Inject constructor(
    private val jankMetric: JankMetric
  ) : OnAppCreate {
    override fun onCreate(app: Application) {
      jankMetric.onAppCreate(app)
    }
  }
}
