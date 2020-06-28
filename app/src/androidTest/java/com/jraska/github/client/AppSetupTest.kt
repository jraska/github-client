package com.jraska.github.client

import io.reactivex.android.schedulers.AndroidSchedulers
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

  @Test
  fun androidSchedulerIsAsync() {
    // This got broken so many times - even reflection might be worth the test
    val handlerScheduler = AndroidSchedulers.mainThread()

    val asyncField = handlerScheduler.javaClass.getDeclaredField("async")
    asyncField.isAccessible = true

    val isAsync = asyncField.get(AndroidSchedulers.mainThread()) as Boolean
    assertThat(isAsync).isTrue()
  }
}

fun recordedEvents() = TestUITestApp.get()
  .coreComponent
  .eventAnalytics
  .events()

