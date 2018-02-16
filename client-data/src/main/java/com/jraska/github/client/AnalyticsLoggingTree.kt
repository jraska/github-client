package com.jraska.github.client

import android.util.Log
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.time.DateTimeProvider
import timber.log.Timber
import javax.inject.Inject

class AnalyticsLoggingTree @Inject constructor(private val config: Config,
                                               private val eventAnalytics: EventAnalytics,
                                               private val dateTimeProvider: DateTimeProvider) : Timber.DebugTree() {


  override fun isLoggable(tag: String?, priority: Int): Boolean {
    val priorityToLog = config.getLong("logging_analytics_priority")
    return (priority >= priorityToLog && priorityToLog != 0L)
  }

  override fun log(priority: Int, tag: String?, message: String, error: Throwable?) {
    val analyticsName = analyticsName(priority)
    val eventBuilder = AnalyticsEvent.builder(analyticsName)
      .addProperty("tag", maxString(tag))
      .addProperty("message", maxString(message))
      .addProperty("time", dateTimeProvider.now())

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

  private fun analyticsName(priority: Int): String {
    return when (priority) {
      Log.VERBOSE -> "verbose"
      Log.DEBUG -> "debug"
      Log.INFO -> "info"
      Log.WARN -> "warning"
      Log.ERROR -> "error"
      Log.ASSERT -> "WTF"

      else -> "unknown"
    }
  }

  companion object {
    const val MAX_LENGTH: Int = 100
  }
}
