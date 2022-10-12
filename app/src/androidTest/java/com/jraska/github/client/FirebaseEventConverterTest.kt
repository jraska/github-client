package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.core.android.firebase.FirebaseEventConverter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class FirebaseEventConverterTest {
  @Test
  fun convertsProperly() {
    val event = AnalyticsEvent.builder(
      AnalyticsEvent.Key("whatever", Owner.CORE_TEAM))
        .addProperty("boolean", true)
        .addProperty("int", 8)
        .addProperty("double", 3.4)
        .addProperty("string", "Hello")
        .addProperty("long", 123L)
        .build()

      val firebaseBundle = FirebaseEventConverter . firebaseBundle (event.properties)!!

    assertThat(firebaseBundle.getInt("boolean")).isEqualTo(1)
    assertThat(firebaseBundle.getDouble("double")).isEqualTo(3.4)
    assertThat(firebaseBundle.getString("string")).isEqualTo("Hello")
    assertThat(firebaseBundle.getInt("int")).isEqualTo(8)
    assertThat(firebaseBundle.getLong("long")).isEqualTo(123)
  }
}
