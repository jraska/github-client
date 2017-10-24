package com.jraska.github.client

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.logging.CrashReporter
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class FakeFirebaseModule : FirebaseModule() {
  internal override fun eventAnalytics(analytics: FirebaseEventAnalytics): EventAnalytics {
    return EventAnalytics.EMPTY
  }

  internal override fun analyticsProperty(analytics: FirebaseEventAnalytics): AnalyticsProperty {
    return mock(AnalyticsProperty::class.java)
  }

  internal override fun firebaseCrash(): CrashReporter {
    return mock(CrashReporter::class.java)
  }

  internal override fun firebaseDatabase(): FirebaseDatabase {
    val mockedDatabase = mock(FirebaseDatabase::class.java)
    `when`(mockedDatabase.getReference(any())).thenReturn(mock(DatabaseReference::class.java))
    return mockedDatabase
  }
}
