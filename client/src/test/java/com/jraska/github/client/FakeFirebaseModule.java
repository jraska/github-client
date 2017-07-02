package com.jraska.github.client;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;
import com.jraska.github.client.analytics.AnalyticsProperty;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.logging.CrashReporter;

import static org.mockito.Mockito.mock;

public class FakeFirebaseModule extends FirebaseModule {
  @Override EventAnalytics eventAnalytics(Context context, Config config) {
    return mock(EventAnalytics.class);
  }

  @Override AnalyticsProperty analyticsProperty(Context context, Config config) {
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
