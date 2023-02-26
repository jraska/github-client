package com.jraska.github.client.http

import okhttp3.HttpUrl.Companion.toHttpUrl
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RequestRejectionRegistryTest {
  @Test
  fun multiThreadedTest() {
    val requestRejectionRegistry = RequestRejectionRegistry()

    val randomHostsUrls = (0..1000).map { "https://randomHost$it.com/path".toHttpUrl() }

    val startAwaitLatch = CountDownLatch(1)
    val endAwaitLatch = CountDownLatch(1)

    val runnable = Runnable {
      randomHostsUrls.parallelStream().forEach {
        startAwaitLatch.countDown()

        requestRejectionRegistry.rejectionProbability(it)
        requestRejectionRegistry.onNextResponse(it, false)
        requestRejectionRegistry.onNextResponse(it, true)
      }

      endAwaitLatch.countDown()
    }

    Thread(runnable).start()

    val awaitResult = startAwaitLatch.await(1, TimeUnit.SECONDS)
    assertThat(awaitResult).isTrue

    randomHostsUrls.forEach {
      requestRejectionRegistry.rejectionProbability(it)
      requestRejectionRegistry.onNextResponse(it, false)
    }

    endAwaitLatch.await(1, TimeUnit.SECONDS)
    randomHostsUrls.forEach {
      assertThat(requestRejectionRegistry.rejectionProbability(it)).isEqualTo(0.25)
    }
  }
}
