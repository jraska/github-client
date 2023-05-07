package com.jraska.github.client.http

import com.jraska.github.client.time.TimeProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HttpTrackingInterceptor @Inject constructor(
  private val reporter: NetworkResourceReporter,
  private val timeProvider: TimeProvider
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val start = timeProvider.elapsed()
    val response = chain.proceed(chain.request())
    val duration = timeProvider.elapsed() - start

    val resource = toResource(response, duration)
    reporter.report(resource)

    return response
  }

  private fun toResource(response: Response, duration: Long): NetworkResource {
    val request = response.request
    val requestId =
      request.header("Request-Id")!! // we prefer crash as this would indicate error in setup

    val requestContentLength = request.headers["Content-Length"]?.toLongOrNull()
      ?: request.body?.contentLength()
      ?: 0

    val responseContentLength = response.headers["Content-Length"]?.toLongOrNull()
      ?: response.body?.contentLength()
      ?: 0

    return NetworkResource(
      url = request.url,
      method = request.method,
      durationMs = duration,
      requestId = requestId,
      statusCode = response.code,
      responseContentLength = responseContentLength,
      message = response.message,
      requestContentLength = requestContentLength,
    )
  }
}
