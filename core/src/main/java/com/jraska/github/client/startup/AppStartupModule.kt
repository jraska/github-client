package com.jraska.github.client.startup

import com.jraska.github.client.core.android.OnAppCreate
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
object AppStartupModule {
  @Provides
  @IntoSet
  internal fun registerStartupCallbacks(create: StartupTimeMetric.RegisterStartupCallbacks): OnAppCreate = create

  @Provides
  @Singleton
  internal fun startupTimeMetric(reporter: StartupAnalyticsReporter): StartupTimeMetric {
    return StartupTimeMetric(reporter)
  }
}
