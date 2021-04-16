package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.logging.CrashReporter
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
object FakeCoreModule {
  val eventAnalytics = Fakes.recordingAnalytics()
  val analyticProperty = Fakes.recordingAnalyticsProperty()

  @Provides
  fun analytics(): EventAnalytics {
    return eventAnalytics
  }

  @Provides
  @Singleton
  fun fakeConfig(): FakeConfig = Fakes.config()

  @Provides
  fun config(fakeConfig: FakeConfig, decorations: Set<@JvmSuppressWildcards Config.Decoration>): Config {
    var config: Config = fakeConfig
    decorations.forEach {
      config = it.decorate(config)
    }

    return config
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
  @IntoSet
  fun configDecoration(): Config.Decoration {
    return object : Config.Decoration {
      override fun decorate(originalConfig: Config): Config {
        return originalConfig
      }
    }
  }
}
