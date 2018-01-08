package com.jraska.github.client

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.logging.CrashReporter
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class FakeCoreComponent : CoreComponent {
  override fun analytics(): EventAnalytics {
    return EventAnalytics.EMPTY
  }

  override fun analyticsProperty(): AnalyticsProperty {
    return mock(AnalyticsProperty::class.java)
  }

  override fun crashReporter(): CrashReporter {
    return mock(CrashReporter::class.java)
  }

  override fun config(): Config {
    return mock(Config::class.java)
  }

  override fun firebaseDatabase(): FirebaseDatabase {
    val mockedDatabase = mock(FirebaseDatabase::class.java)
    `when`(mockedDatabase.getReference(any())).thenReturn(mock(DatabaseReference::class.java))
    return mockedDatabase
  }
}
