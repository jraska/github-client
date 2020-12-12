package com.jraska.analytics

class ConsoleReporter(private val reporterName: String) : AnalyticsReporter {
  override fun report(vararg events: AnalyticsEvent) {
    events.forEach {
      println("$reporterName: $it")
    }
  }
}
