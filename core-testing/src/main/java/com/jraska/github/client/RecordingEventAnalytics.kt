package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics

class RecordingEventAnalytics : EventAnalytics {
  private val events = ArrayList<AnalyticsEvent>()

  override fun report(event: AnalyticsEvent) {
    events.add(event)
  }

  fun events(): List<AnalyticsEvent> {
    return ArrayList(events)
  }
}
