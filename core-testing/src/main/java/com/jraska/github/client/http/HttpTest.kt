package com.jraska.github.client.http

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.ExternalResource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object HttpTest {
  private val DEFAULT_BASE_URL = "https://api.github.com".toHttpUrl()

  fun retrofit(mockWebServer: MockWebServer? = null): Retrofit {
    return Retrofit.Builder()
      .baseUrl(mockWebServer?.url("/") ?: DEFAULT_BASE_URL)
      .client(client(mockWebServer))
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  private fun client(mockWebServer: MockWebServer?): OkHttpClient {
    return OkHttpClient.Builder()
      .also { if (mockWebServer == null) it.addInterceptor(MockWebServerInterceptor) }
      .addInterceptor(HttpLoggingInterceptor { println(it) }.apply {
        level = HttpLoggingInterceptor.Level.BASIC
      }).build()
  }
}

fun MockWebServer.enqueue(path: String) {
  this.enqueue(MockResponse().setBody(json(path)))
}

fun MockWebServer.onUrlPartReturn(urlPart: String, jsonPath: String) = onUrlPartReturn(urlPart, jsonResource(jsonPath))

fun MockWebServer.onUrlPartReturn(urlPart: String, mockResponse: MockResponse) {
  ensureMapDispatcher()

  (dispatcher as MapDispatcher).onMatchingReturn(UrlContainsMatcher(urlPart), mockResponse)
}

fun MockWebServer.onUrlReturn(urlRegex: Regex, jsonPath: String) = onUrlReturn(urlRegex, jsonResource(jsonPath))

fun MockWebServer.onUrlReturn(urlRegex: Regex, mockResponse: MockResponse) {
  ensureMapDispatcher()

  (dispatcher as MapDispatcher).onMatchingReturn(UrlRegexMatcher(urlRegex), mockResponse)
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

fun jsonResource(path: String): MockResponse {
  return MockResponse().setBody(json(path))
}

object MockWebServerInterceptor : Interceptor {
  var mockWebServer: MockWebServer? = null

  override fun intercept(chain: Interceptor.Chain): Response {
    val webServer = mockWebServer ?: throw UnsupportedOperationException("You are trying to do network requests in tests you naughty developer!")

    val newRequest = chain.request().newBuilder().url(webServer.url(chain.request().url.encodedPath)).build()
    return chain.proceed(newRequest)
  }
}

class MockWebServerInterceptorRule(private val mockWebServer: MockWebServer) : ExternalResource() {
  override fun before() {
    MockWebServerInterceptor.mockWebServer = mockWebServer
  }

  override fun after() {
    MockWebServerInterceptor.mockWebServer = null
  }
}

