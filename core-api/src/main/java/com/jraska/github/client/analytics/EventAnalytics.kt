package com.jraska.github.client.analytics

interface EventAnalytics {
  fun report(event: AnalyticsEvent)
}
