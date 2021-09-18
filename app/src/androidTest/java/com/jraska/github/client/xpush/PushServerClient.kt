package com.jraska.github.client.xpush

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import timber.log.Timber

interface PushServerClient {
  @POST("/fcm/send")
  fun sendPush(@Body message: PushServerDto): Call<ResponseBody>

  companion object {
    fun create(authorizationToken: String): PushServerClient {
      return Retrofit.Builder()
        .baseUrl("https://fcm.googleapis.com")
        .client(pushClient(authorizationToken))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PushServerClient::class.java)
    }

    private fun pushClient(authorizationToken: String) = OkHttpClient.Builder()
      .addInterceptor(AddAuthorizationHeaderInterceptor(authorizationToken))
      .addInterceptor(HttpLoggingInterceptor { Timber.d(it) }.setLevel(HttpLoggingInterceptor.Level.BASIC))
      .build()
  }

  private class AddAuthorizationHeaderInterceptor(private val value: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
      val newRequest = chain.request().newBuilder()
        .addHeader("Authorization", "key=$value")
        .build()

      return chain.proceed(newRequest)
    }
  }
}
