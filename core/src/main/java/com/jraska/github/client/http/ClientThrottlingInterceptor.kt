package com.jraska.github.client.http

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.EMPTY_RESPONSE
import timber.log.Timber
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

const val K_ACCEPTS_MULTIPLIER = 2.0

// https://sre.google/sre-book/handling-overload/
class ClientThrottlingInterceptor(
  private val random: Random = Random()
) : Interceptor {

  private val requestsCount = AtomicInteger()
  private val accepts = AtomicInteger()

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()

    val clientRejectionNeverReachingServer = nextClientRejectionResponse(request)
    if (clientRejectionNeverReachingServer != null) {

      Timber.v("Client rejecting url: %s", request.url)
      return clientRejectionNeverReachingServer
    }

    val response = chain.proceed(request)

    requestsCount.incrementAndGet()
    if (isAccepted(response)) {
      accepts.incrementAndGet()
    }

    return response
  }

  private fun nextClientRejectionResponse(request: Request): Response? {
    if (shouldRejectRequest(request)) {
      return clientRejectionResponse(request)
    }

    return null
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

  private fun shouldRejectRequest(request: Request): Boolean {
    return random.nextDouble() < rejectionProbability(request)
  }

  private fun rejectionProbability(request: Request): Double {
    // we don't synchronize between accepts and requests count - since we count probability,
    // they don't need to be necessarily in sync and small diffs are fine

    return (requestsCount.get() - (K_ACCEPTS_MULTIPLIER * accepts.get())) / (requestsCount.get() + 1)
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
}
