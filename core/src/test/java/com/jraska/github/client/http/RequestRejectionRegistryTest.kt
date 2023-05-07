package com.jraska.github.client.http

import com.jraska.github.client.TestTimeProvider
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RequestRejectionRegistryTest {
  @Test
  fun multiThreadedHostsTest() {
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

  @Test
  fun requestsStaleByTime() {
    val testTimeProvider = TestTimeProvider()
    val url = "https://api.github.com/v3".toHttpUrl()

    val registry = RequestRejectionRegistry(testTimeProvider)

    repeat(6) { registry.onNextResponse(url, false) }
    repeat(3) { registry.onNextResponse(url, true) }

    val rejectionProbability = registry.rejectionProbability(url)
    assertThat(rejectionProbability).isEqualTo(0.3)

    testTimeProvider.advanceTime(STALE_REQUEST_RECORD_TIME - 1)
    assertThat(registry.rejectionProbability(url)).isEqualTo(0.3)
    testTimeProvider.advanceTime(1)
    assertThat(registry.rejectionProbability(url)).isEqualTo(0.0)
  }

  @Test
  fun requestsStaleByTimeRolling() {
    val testTimeProvider = TestTimeProvider()
    val url = "https://api.github.com/v3".toHttpUrl()

    val registry = RequestRejectionRegistry(testTimeProvider)

    registry.onNextResponse(url, false)
    testTimeProvider.advanceTime(10)
    registry.onNextResponse(url, false)
    testTimeProvider.advanceTime(10)
    registry.onNextResponse(url, false)
    registry.onNextResponse(url, true)
    testTimeProvider.advanceTime(10)
    registry.onNextResponse(url, false)
    registry.onNextResponse(url, false)
    testTimeProvider.advanceTime(10)
    registry.onNextResponse(url, true)

    assertThat(registry.rejectionProbability(url)).isEqualTo(0.375)

    testTimeProvider.elapsed = STALE_REQUEST_RECORD_TIME
    assertThat(registry.rejectionProbability(url)).isEqualTo(0.28, Offset.offset(0.01))

    testTimeProvider.advanceTime(10)
    assertThat(registry.rejectionProbability(url)).isEqualTo(0.16, Offset.offset(0.01))

    testTimeProvider.advanceTime(10)
    assertThat(registry.rejectionProbability(url)).isEqualTo(0.25)

    testTimeProvider.advanceTime(9)
    assertThat(registry.rejectionProbability(url)).isEqualTo(0.25)

    testTimeProvider.advanceTime(1)
    assertThat(registry.rejectionProbability(url)).isEqualTo(0.0)

    testTimeProvider.advanceTime(Int.MAX_VALUE.toLong())
    assertThat(registry.rejectionProbability(url)).isEqualTo(0.0)
  }
}
