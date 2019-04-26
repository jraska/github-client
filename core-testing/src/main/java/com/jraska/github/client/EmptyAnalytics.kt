package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics

class EmptyAnalytics : EventAnalytics {
  override fun report(event: AnalyticsEvent) {
    // do nothing
  }
}
