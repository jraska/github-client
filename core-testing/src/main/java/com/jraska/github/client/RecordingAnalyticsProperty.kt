package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsProperty

class RecordingAnalyticsProperty : AnalyticsProperty {
  private val properties = mutableMapOf<String, String>()

  fun properties(): Map<String, String> = properties

  override fun setUserProperty(key: String, value: String) {
    properties[key] = value
  }
}
