package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsProperty

class RecordingAnalyticsProperty : AnalyticsProperty {
  private val properties = mutableMapOf<String, String>()

  override fun setUserProperty(key: String, value: String) {
    properties[key] = value
  }
}
