package com.jraska.analytics

class ConsoleReporter(private val reporterName: String) : AnalyticsReporter {
  override val name = "Console"

  override fun report(vararg events: AnalyticsEvent) {
    events.forEach {
      println("$reporterName: $it")
    }
  }
}
