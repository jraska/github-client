package com.jraska.github.client.core.android.logging

import android.util.Log
import com.jraska.github.client.Config
import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.time.DateTimeProvider
import timber.log.Timber
import javax.inject.Inject

class AnalyticsLoggingTree @Inject constructor(
  private val config: Config,
  private val eventAnalytics: EventAnalytics,
  private val dateTimeProvider: DateTimeProvider
) : Timber.DebugTree() {

  private val analyticsKeyMap by lazy {
    mapOf(
      Log.VERBOSE to AnalyticsEvent.Key("verbose", Owner.CORE_TEAM),
      Log.DEBUG to AnalyticsEvent.Key("debug", Owner.CORE_TEAM),
      Log.INFO to AnalyticsEvent.Key("info", Owner.CORE_TEAM),
      Log.WARN to AnalyticsEvent.Key("warning", Owner.CORE_TEAM),
      Log.ERROR to AnalyticsEvent.Key("error", Owner.CORE_TEAM),
      Log.ASSERT to AnalyticsEvent.Key("WTF", Owner.CORE_TEAM)
    ).withDefault { AnalyticsEvent.Key("unknown", Owner.CORE_TEAM) }
  }

  override fun isLoggable(tag: String?, priority: Int): Boolean {
    val priorityToLog = config.getLong(LOGGING_PRIORITY)
    return (priority >= priorityToLog && priorityToLog != 0L)
  }

  override fun log(priority: Int, tag: String?, message: String, error: Throwable?) {
    val analyticsKey = analyticsKeyMap.getValue(priority)
    val eventBuilder = AnalyticsEvent.builder(analyticsKey)
      .addProperty("tag", maxString(tag))
      .addProperty("message", maxString(message))
      .addProperty("time", dateTimeProvider.now().toString())

    if (error != null) {
      eventBuilder.addProperty("error", maxString(error.javaClass.name))
      eventBuilder.addProperty("errorMessage", maxString(error.message))
    }

    eventAnalytics.report(eventBuilder.build())
  }

  private fun maxString(obj: Any?): String {
    if (obj == null) {
      return "null"
    }

    val value = obj.toString()
    return if (value.length < MAX_LENGTH) {
      value
    } else {
      value.substring(0, MAX_LENGTH)
    }
  }

  companion object {
    const val MAX_LENGTH: Int = 100
    val LOGGING_PRIORITY = Config.Key("logging_analytics_priority", Owner.CORE_TEAM)
  }
}
