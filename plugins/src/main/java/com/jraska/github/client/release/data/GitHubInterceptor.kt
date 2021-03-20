package com.jraska.github.client.release.data

import okhttp3.Interceptor
import okhttp3.Response

class GitHubInterceptor(
  private val apiToken: String
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val newRequest = chain.request().newBuilder()
      .addHeader("Authorization", "token $apiToken")
      .addHeader("Accept", "application/vnd.github.v3+json")
      .build()

    return chain.proceed(newRequest)
  }
}
