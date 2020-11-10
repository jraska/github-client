package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.logging.CrashReporter
import dagger.Module
import dagger.Provides

@Module
object FakeCoreModule {
  val eventAnalytics = Fakes.recordingAnalytics()
  val config = Fakes.config()
  val analyticProperty = Fakes.recordingAnalyticsProperty()

  @Provides
  fun analytics(): EventAnalytics {
    return eventAnalytics
  }

  @Provides
  fun analyticsProperty(): AnalyticsProperty {
    return analyticProperty
  }

  @Provides
  fun crashReporter(): CrashReporter {
    return EmptyCrashReporter
  }

  @Provides
  fun config(): Config {
    return config
  }
}
