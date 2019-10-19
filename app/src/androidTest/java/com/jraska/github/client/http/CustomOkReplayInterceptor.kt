package com.jraska.github.client.http

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import okreplay.OkReplayConfig
import okreplay.OkReplayInterceptor
import okreplay.Tape

class CustomOkReplayInterceptor : OkReplayInterceptor() {
  private val ordinalsInterceptor = AddOrdinalParameterInterceptor()

  override fun start(configuration: OkReplayConfig?, tape: Tape?) {
    ordinalsInterceptor.clear()
    super.start(configuration, tape)
  }

  override fun stop() {
    super.stop()
    ordinalsInterceptor.clear()
  }

  fun interceptorsToRegister(): List<Interceptor> {
    return listOf(ordinalsInterceptor, this, RemoveOrdinalParameterInterceptor())
  }
}

const val SYNTHETIC_PARAMETER_NAME = "syntheticOkReplayOrdinal"

private class RemoveOrdinalParameterInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val url = request.url
    if (url.queryParameter(SYNTHETIC_PARAMETER_NAME) == null) {
      return chain.proceed(request)
    }

    val newRequest = request.newBuilder()
      .url(url.newBuilder()
        .removeAllQueryParameters(SYNTHETIC_PARAMETER_NAME)
        .build())
      .build()

    return chain.proceed(newRequest)
  }
}

private class AddOrdinalParameterInterceptor : Interceptor {
  private val requestCounts = HashMap<HttpUrl, Int>()

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val url = request.url

    val requestsPerformed = requestCounts[url]
    if (requestsPerformed == null) {
      requestCounts[url] = 1
      return chain.proceed(request) // do not add anything, first request
    } else {
      val requestOrdinal = requestsPerformed + 1
      requestCounts[url] = requestOrdinal

      // we add synthetic parameter to distinguish request
      val newRequest = request.newBuilder()
        .url(url.newBuilder()
          .addQueryParameter(SYNTHETIC_PARAMETER_NAME, requestOrdinal.toString())
          .build())
        .build()
      return chain.proceed(newRequest)
    }
  }

  fun clear() {
    requestCounts.clear()
  }
}
