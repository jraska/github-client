package com.jraska.github.client.performance.startup

import android.os.Process
import android.os.SystemClock
import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import timber.log.Timber
import javax.inject.Inject

class StartupAnalyticsReporter @Inject constructor(
  private val eventAnalytics: EventAnalytics
) {
  fun reportForegroundLaunch(launchedActivity: String, startupSavedStatePresent: Boolean) {
    val launchTimeNow = launchTimeNow()
    Timber.d("AppStartup is %s ms, activity: %s, savedState: %s", launchTimeNow, launchedActivity, startupSavedStatePresent)

    val eventBuilder = if (startupSavedStatePresent) {
      AnalyticsEvent.builder(ANALYTICS_AFTER_KILL_START)
    } else {
      AnalyticsEvent.builder(ANALYTICS_COLD_START)
    }

    eventBuilder.addProperty("time", launchTimeNow)
    eventBuilder.addProperty("activity", launchedActivity)

    eventAnalytics.report(eventBuilder.build())
  }

  fun reportBackgroundLaunch() {
    Timber.d("AppStartup in the background")

    eventAnalytics.report(AnalyticsEvent.create(ANALYTICS_BACKGROUND_START))
  }

  fun reportForegroundLaunchWithoutActivity() {
    Timber.d("App started as foreground, but post ran before any Activity onCreate()")

    eventAnalytics.report(AnalyticsEvent.create(ANALYTICS_UNDEFINED))
  }

  private fun launchTimeNow() = SystemClock.uptimeMillis() - Process.getStartUptimeMillis()

  companion object {
    val ANALYTICS_COLD_START = AnalyticsEvent.Key("start_cold", Owner.PERFORMANCE_TEAM)
    val ANALYTICS_AFTER_KILL_START = AnalyticsEvent.Key("start_warm_after_kill", Owner.PERFORMANCE_TEAM)
    val ANALYTICS_BACKGROUND_START = AnalyticsEvent.Key("start_background", Owner.PERFORMANCE_TEAM)
    val ANALYTICS_UNDEFINED = AnalyticsEvent.Key("start_foreground_undefined", Owner.PERFORMANCE_TEAM)
  }
}
