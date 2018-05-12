package com.jraska.github.client

import com.google.firebase.analytics.FirebaseAnalytics
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics

internal class FirebaseEventAnalytics(private val analytics: FirebaseAnalytics) : EventAnalytics, AnalyticsProperty {

  override fun setUserProperty(key: String, value: String) {
    analytics.setUserProperty(key, value)
  }

  override fun report(event: AnalyticsEvent) {
    val parameters = FirebaseEventConverter.firebaseBundle(event.properties)
    analytics.logEvent(event.name, parameters)
  }
}
