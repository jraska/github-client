package com.jraska.github.client.http

import com.jraska.github.client.AppVersion
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class AppCommonHeadersInterceptorTest {
  @get:Rule
  val mockWebServer = MockWebServer()

  @Test
  fun addsRequiredHeaders() {
    mockWebServer.enqueue(MockResponse())
    mockWebServer.enqueue(MockResponse())

    val client = okHttpClient()

    val request = Request.Builder().method("GET", null)
      .url(mockWebServer.url("/"))
      .build()

    val firstHeaders = client.newCall(request).execute().request.headers

    assertThat(firstHeaders["User-Agent"]).isEqualTo("Android-GitHubClient/1.2.3-dev")
    assertThat(firstHeaders["Sequence-Number"]).isEqualTo("1")
    assertThat(firstHeaders["Request-Id"]).isNotEmpty

    val secondHeaders = client.newCall(request).execute().request.headers
    assertThat(secondHeaders["Sequence-Number"]).isEqualTo("2")
    assertThat(firstHeaders["Request-Id"]).isNotEqualTo(secondHeaders["Request-Id"])
  }

  private fun okHttpClient(): OkHttpClient {
    val interceptor = AppCommonHeadersInterceptor(object : AppVersion {
      override fun appVersion(): String {
        return "1.2.3-dev"
      }
    })

    return OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .build()
  }
}
