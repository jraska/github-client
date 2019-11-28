package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.logging.CrashReporter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class])
interface FirebaseCoreComponent : CoreComponent{
  override fun crashReporter(): CrashReporter

  override fun config(): Config

  override fun analyticsProperty(): AnalyticsProperty

  override fun analytics(): EventAnalytics
}
