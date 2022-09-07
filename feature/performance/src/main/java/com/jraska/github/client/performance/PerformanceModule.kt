package com.jraska.github.client.performance

import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.performance.jank.JankMetric
import com.jraska.github.client.performance.startup.StartupAnalyticsReporter
import com.jraska.github.client.performance.startup.StartupTimeMetric
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
object PerformanceModule {
  @Provides
  @IntoSet
  internal fun registerStartupCallbacks(create: StartupTimeMetric.RegisterStartupCallbacks): OnAppCreate = create

  @Provides
  @Singleton
  internal fun startupTimeMetric(reporter: StartupAnalyticsReporter): StartupTimeMetric {
    return StartupTimeMetric(reporter)
  }

  @Provides
  @IntoSet
  internal fun startupJank(create: JankMetric.StartupOnCreate): OnAppCreate = create
}
