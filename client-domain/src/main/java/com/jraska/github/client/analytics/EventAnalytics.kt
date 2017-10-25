package com.jraska.github.client.analytics

interface EventAnalytics {
  fun report(event: AnalyticsEvent)

  class EmptyAnalytics : EventAnalytics {
    override fun report(event: AnalyticsEvent) {
      // does nothing
    }
  }

  companion object {
    val EMPTY = EmptyAnalytics()
  }
}
