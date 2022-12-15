package com.jraska.github.client.identity.internal

import com.jraska.github.client.identity.IdentityProvider
import okhttp3.Interceptor
import okhttp3.Response

class AddSessionIdInterceptor(
  private val identityProvider: IdentityProvider
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val newRequest = chain.request()
      .newBuilder()
      .addHeader("Session-Id", identityProvider.session().id.toString())
      .build()

    return chain.proceed(newRequest)
  }
}
