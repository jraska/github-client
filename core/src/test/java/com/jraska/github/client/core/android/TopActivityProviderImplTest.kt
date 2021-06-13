package com.jraska.github.client.core.android

import android.app.Activity
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class TopActivityProviderImplTest {

  val activity = Activity()

  @Test
  fun handlesMultiThreading() {
    testMultiThreadAccess(1, 1000)
  }

  @Test
  fun handlesMultiThreading2() {
    testMultiThreadAccess(2, 1000)
  }

  @Test
  fun handlesMultiThreading3() {
    testMultiThreadAccess(5, 1000)
  }

  private fun testMultiThreadAccess(threadsCount: Int, iterationsCount: Int) {
    val testThread = Thread.currentThread()
    val topActivityProvider = TopActivityProviderImpl { Thread.currentThread() == testThread }

    val countDownLatch = CountDownLatch(threadsCount)
    val counter = NonAtomicInteger()

    val runnable = Runnable {
      for (iteration in 1..iterationsCount) {
        topActivityProvider.onTopActivity { counter.increment() }
        if (iteration % 10 == 7) {
          Thread.sleep(1)
        }
      }
      countDownLatch.countDown()
    }

    for (threadIndex in 1..threadsCount) {
      Thread(runnable).start()
    }

    topActivityProvider.callbacks.onActivityCreated(activity, null)

    while (!countDownLatch.await(5, TimeUnit.MICROSECONDS)) {
      topActivityProvider.callbacks.onActivityStarted(activity)
      topActivityProvider.callbacks.onActivityResumed(activity)
      topActivityProvider.callbacks.onActivityPaused(activity)
      topActivityProvider.callbacks.onActivityStopped(activity)
    }
    topActivityProvider.callbacks.onActivityStarted(activity)

    assertThat(counter.value).isEqualTo(threadsCount * iterationsCount)
  }
}

class NonAtomicInteger {
  var value: Int = 0

  fun increment() {
    value++
  }
}
