package com.jraska.github.client;

import com.google.firebase.database.FirebaseDatabase;
import com.jraska.github.client.analytics.AnalyticsProperty;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.logging.CrashReporter;

import static org.mockito.Mockito.mock;

public class FakeFirebaseModule extends FirebaseModule {
  @Override EventAnalytics eventAnalytics(FirebaseEventAnalytics analytics) {
    return EventAnalytics.EMPTY;
  }

  @Override AnalyticsProperty analyticsProperty(FirebaseEventAnalytics analytics) {
    return mock(AnalyticsProperty.class);
  }

  @Override CrashReporter firebaseCrash() {
    return mock(CrashReporter.class);
  }

  @Override Config config() {
    return mock(Config.class);
  }

  @Override FirebaseDatabase firebaseDatabase() {
    return mock(FirebaseDatabase.class);
  }
}
