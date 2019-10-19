package com.jraska.github.client

import com.google.firebase.database.FirebaseDatabase
import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.logging.CrashReporter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class])
interface CoreComponent {
  fun crashReporter(): CrashReporter

  fun config(): Config

  fun analyticsProperty(): AnalyticsProperty

  fun analytics(): EventAnalytics

  fun firebaseDatabase(): FirebaseDatabase
}
