package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.logging.CrashReporter

interface CoreComponent {
  fun crashReporter(): CrashReporter

  fun config(): Config

  fun analyticsProperty(): AnalyticsProperty

  fun analytics(): EventAnalytics
}
