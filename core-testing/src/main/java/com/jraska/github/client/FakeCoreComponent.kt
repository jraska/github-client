package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.logging.CrashReporter

class FakeCoreComponent : CoreComponent {
  val eventAnalytics = Fakes.recordingAnalytics()
  val config = Fakes.config()
  val analyticProperty = Fakes.recordingAnalyticsProperty()

  override fun analytics(): EventAnalytics {
    return eventAnalytics
  }

  override fun analyticsProperty(): AnalyticsProperty {
    return analyticProperty
  }

  override fun crashReporter(): CrashReporter {
    return Fakes.emptyCrashReporter()
  }

  override fun config(): Config {
    return config
  }
}
