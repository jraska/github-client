package com.jraska.github.client.xpush

import io.reactivex.Completable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import timber.log.Timber

interface PushServerClient {
  @POST("/fcm/send")
  fun sendPush(@Body message: PushServerDto): Completable

  companion object {
    fun create(authorizationToken: String): PushServerClient {
      return Retrofit.Builder().validateEagerly(true)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://fcm.googleapis.com")
        .client(
          OkHttpClient.Builder()
            .addInterceptor { chain ->
              chain.proceed(
                chain.request().newBuilder()
                  .addHeader("Authorization", "key=$authorizationToken")
                  .build()
              )
            }
            .addInterceptor(HttpLoggingInterceptor { Timber.d(it) }.setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()
        )
        .build()
        .create(PushServerClient::class.java)
    }
  }
}
