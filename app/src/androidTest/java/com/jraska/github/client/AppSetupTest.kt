package com.jraska.github.client

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import timber.log.Timber

class AppSetupTest {
  @Test
  fun timberHasTrees() {
    val forest = Timber.forest()

    assertThat(forest).isNotEmpty()
  }

  @Test
  fun appCreateEventFired() {
    val event = recordedEvents().findLast { event -> event.name == "app_create" }

    assertThat(event).isNotNull
  }
}

fun recordedEvents() = FakeCoreModule
  .eventAnalytics
  .events()

