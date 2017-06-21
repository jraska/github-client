package com.jraska.github.client;

import android.content.Context;

import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.logging.CrashReporter;

import static org.mockito.Mockito.mock;

public class FakeFirebaseModule extends FirebaseModule {
  @Override EventAnalytics eventAnalytics(Context context, Config config) {
    return mock(EventAnalytics.class);
  }

  @Override CrashReporter firebaseCrash() {
    return mock(CrashReporter.class);
  }

  @Override Config config() {
    return mock(Config.class);
  }
}
