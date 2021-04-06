package com.jraska.github.client.http

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object HttpTest {
  fun retrofit(baseUrl: HttpUrl): Retrofit {
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor { println(it) }.apply {
        level = HttpLoggingInterceptor.Level.BASIC
      }).build())
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
  }
}

fun MockWebServer.enqueue(path: String) {
  this.enqueue(MockResponse().setBody(json(path)))
}

fun MockWebServer.onUrlPartReturn(urlPart: String, jsonPath: String) {
  ensureMapDispatcher()

  (dispatcher as MapDispatcher).onUrlPartReturn(urlPart, jsonPath)
}

fun MockWebServer.onUrlReturn(urlRegex: Regex, jsonPath: String) {
  ensureMapDispatcher()

  (dispatcher as MapDispatcher).onUrlReturn(urlRegex, jsonPath)
}

private fun MockWebServer.ensureMapDispatcher() {
  if (dispatcher !is MapDispatcher) {
    dispatcher = MapDispatcher()
  }
}

internal fun json(path: String): String {
  val uri = HttpTest.javaClass.classLoader.getResource(path)
  val file = File(uri?.path!!)
  return String(file.readBytes())
}

