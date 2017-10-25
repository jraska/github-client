package com.jraska.github.client

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics

internal class FirebaseEventAnalytics(private val analytics: FirebaseAnalytics) : EventAnalytics, AnalyticsProperty {

  override fun setUserProperty(key: String, value: String) {
    analytics.setUserProperty(key, value)
  }

  override fun report(event: AnalyticsEvent) {
    val parameters = propertiesBundle(event.properties)
    analytics.logEvent(event.name, parameters)
  }

  private fun propertiesBundle(properties: Map<String, String>): Bundle? {
    if (properties.isEmpty()) {
      return null
    }

    val parameters = Bundle()
    for ((key, value) in properties) {
      parameters.putString(key, value)
    }

    return parameters
  }
}
