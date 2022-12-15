package com.jraska.github.client.http

import com.jraska.github.client.AppVersion
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class AppCommonHeadersInterceptor @Inject constructor(
  private val appVersion: AppVersion
) : Interceptor {
  private val sequenceNumber = AtomicInteger(0)

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()

    val newRequest = request.newBuilder()
      .addHeader("Sequence-Number", nextSequenceNumber().toString())
      .addHeader("User-Agent", userAgent())
      .addHeader("Request-Id", UUID.randomUUID().toString())
      .build()

    return chain.proceed(newRequest)
  }

  private fun userAgent(): String {
    return "Android-GitHubClient/${appVersion.appVersion()}"
  }

  private fun nextSequenceNumber() = sequenceNumber.incrementAndGet()
}
