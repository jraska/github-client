package com.jraska.github.client.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jraska.github.client.PerApp;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class AnalyticsModule {
  @Provides @PerApp
  public static EventReporter viewTrigger(FirebaseAnalytics analytics) {
    return new FirebaseEventReporter(analytics);
  }
}
