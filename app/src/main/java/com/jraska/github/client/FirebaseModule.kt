package com.jraska.github.client

import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.logging.CrashReporter
import com.jraska.github.client.logging.FirebaseCrashlyticsReporter
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Singleton

@Module
class FirebaseModule {

  @Provides
  @Singleton internal fun firebaseAnalytics(config: Config): FirebaseEventAnalytics {
    val firebaseAnalytics = FirebaseAnalytics.getInstance(FirebaseApp.getInstance().applicationContext)

    if (config.getBoolean("analytics_disabled")) {
      firebaseAnalytics.setAnalyticsCollectionEnabled(false)
      Timber.d("Analytics disabled")
    } else {
      firebaseAnalytics.setAnalyticsCollectionEnabled(true)
      Timber.d("Analytics enabled")
    }

    return FirebaseEventAnalytics(firebaseAnalytics)
  }

  @Provides internal fun eventAnalytics(analytics: FirebaseEventAnalytics): EventAnalytics {
    return analytics
  }

  @Provides internal fun analyticsProperty(analytics: FirebaseEventAnalytics): AnalyticsProperty {
    return analytics
  }

  @Provides
  @Singleton
  internal fun firebaseCrash(): CrashReporter {
    return FirebaseCrashlyticsReporter()
  }

  @Provides
  @Singleton internal fun config(): Config {
    val configProxy = FirebaseConfigProxy(FirebaseRemoteConfig.getInstance())

    configProxy.setupDefaults().fetch()

    return configProxy
  }
}
