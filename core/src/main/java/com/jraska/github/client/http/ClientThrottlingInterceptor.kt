package com.jraska.github.client.http

import com.jraska.github.client.time.RealTimeProvider
import com.jraska.github.client.time.TimeProvider
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.EMPTY_RESPONSE
import timber.log.Timber
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

const val K_ACCEPTS_MULTIPLIER = 2.0
const val STALE_REQUEST_RECORD_TIME = 2 * 60 * 1000L

// https://sre.google/sre-book/handling-overload/
class ClientThrottlingInterceptor(
  private val random: Random = Random.Default
) : Interceptor {
  private val registry = RequestRejectionRegistry()

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()

    if (random.nextDouble() < registry.rejectionProbability(request.url)) {
      Timber.v("Client rejecting url: %s", request.url)
      return clientRejectionResponse(request)
    }

    val response = chain.proceed(request)
    registry.onNextResponse(response.request.url, isAccepted(response))

    return response
  }

  /**
   * We will use GitHub rate limiting as an example. Real throttling would use a more explicit way based on backend.
   *
   * GitHub returns 403 with x-ratelimit-remaining = "0":
   *
   */
  private fun isAccepted(response: Response): Boolean {
    return !(response.code == 403 && response.headers["x-ratelimit-remaining"] == "0")
  }

  private fun clientRejectionResponse(request: Request): Response {
    return Response.Builder()
      .request(request)
      .protocol(Protocol.HTTP_1_1)
      .code(403)
      .message("Client rejected, server does not accept requests")
      .body(EMPTY_RESPONSE)
      .sentRequestAtMillis(-1L)
      .receivedResponseAtMillis(System.currentTimeMillis())
      .build()
  }
}

internal class RequestRejectionRegistry(
  private val timeProvider: TimeProvider = RealTimeProvider.INSTANCE
) {

  private val requestsRegistry = mutableMapOf<String, RequestData>()

  fun rejectionProbability(url: HttpUrl): Double {
    val requestData = requestsRegistry[url.host] ?: return 0.0

    removeStaleRequestsRecords(requestData)

    return requestData.rejectionProbability()
  }

  private fun removeStaleRequestsRecords(requestData: RequestData) {
    val now = timeProvider.elapsed()
    while (true) {
      val peek = requestData.timeStamps.peek() ?: break

      if (now - peek.timeStamp < STALE_REQUEST_RECORD_TIME) {
        break
      }

      // remove is used for concurrency case when multiple threads detect same stale record
      val removed = requestData.timeStamps.remove(peek)
      if (removed) {
        requestData.count.decrementAndGet()
        if (peek.accepted) {
          requestData.accepts.decrementAndGet()
        }
      }
    }
  }

  fun onNextResponse(url: HttpUrl, accepted: Boolean) {
    val requestData = requestData(url.host)

    requestData.count.incrementAndGet()
    if (accepted) {
      requestData.accepts.incrementAndGet()
    }

    val requestRecord = RequestRecord(timeProvider.elapsed(), accepted)
    requestData.timeStamps.offer(requestRecord)
  }

  private fun requestData(host: String): RequestData {
    val requestData = requestsRegistry[host]
    if (requestData != null) {
      return requestData
    }

    return synchronized(requestsRegistry) {
      requestsRegistry[host]
        ?: RequestData().also { requestsRegistry[host] = it }
    }
  }

  private class RequestData {
    val count = AtomicInteger()
    val accepts = AtomicInteger()
    val timeStamps: Queue<RequestRecord> = ConcurrentLinkedQueue()

    fun rejectionProbability(): Double {
      val value =
        (count.get() - (K_ACCEPTS_MULTIPLIER * accepts.get())) / (count.get() + 1)
      return maxOf(value, 0.0)
    }
  }

  private class RequestRecord(
    val timeStamp: Long,
    val accepted: Boolean
  )
}
