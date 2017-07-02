package com.jraska.github.client;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.logging.CrashReporter;

import static org.mockito.Mockito.mock;

public class FakeFirebaseModule extends FirebaseModule {
  @Override EventAnalytics eventAnalytics(Context context, Config config) {
    return EventAnalytics.EMPTY;
  }

  @Override CrashReporter firebaseCrash() {
    return mock(CrashReporter.class);
  }

  @Override FirebaseDatabase firebaseDatabase() {
    return mock(FirebaseDatabase.class);
  }
}
