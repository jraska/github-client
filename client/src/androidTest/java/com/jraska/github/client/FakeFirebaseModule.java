package com.jraska.github.client;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jraska.github.client.analytics.AnalyticsProperty;
import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.logging.CrashReporter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

  @Override FirebaseDatabase firebaseDatabase() {
    FirebaseDatabase mockedDatabase = mock(FirebaseDatabase.class);
    when(mockedDatabase.getReference(any())).thenReturn(mock(DatabaseReference.class));
    return mockedDatabase;
  }
}
