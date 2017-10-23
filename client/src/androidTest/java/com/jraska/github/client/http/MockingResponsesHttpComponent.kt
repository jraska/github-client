package com.jraska.github.client.http

import android.support.test.espresso.IdlingRegistry
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MockingResponsesHttpComponent private constructor(private val retrofit: Retrofit) : HttpComponent {

  override fun retrofit(): Retrofit {
    return retrofit
  }

  companion object {
    private val NETWORK_ERROR_MESSAGE = "You are trying to do network requests in tests you naughty developer!"

    fun create(): MockingResponsesHttpComponent {
      val idlingResourceFactory = RxHttpIdlingResourceFactory.create()
      IdlingRegistry.getInstance().register(idlingResourceFactory.idlingResource())

      val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addCallAdapterFactory(idlingResourceFactory)
        .addConverterFactory(GsonConverterFactory.create())
        .client(failingClient())
        .build()

      return MockingResponsesHttpComponent(retrofit)
    }

    private fun failingClient(): OkHttpClient {
      return OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .addInterceptor(MockResponsesInterceptor())
        .addInterceptor { _ -> throw UnsupportedOperationException(NETWORK_ERROR_MESSAGE) }
        .build()
    }
  }
}
