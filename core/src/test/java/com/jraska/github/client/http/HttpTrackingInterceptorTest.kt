package com.jraska.github.client.http

import com.jraska.github.client.Fakes
import com.jraska.github.client.TestTimeProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class HttpTrackingInterceptorTest {

  private val mockWebServer = MockWebServer()
  private val testTimeProvider = TestTimeProvider()
  private val recordingAnalytics = Fakes.recordingAnalytics()

  private lateinit var client: OkHttpClient

  @Before
  fun setUp() {
    val httpTrackingInterceptor =
      HttpTrackingInterceptor(NetworkResourceReporter(recordingAnalytics), testTimeProvider)

    client = OkHttpClient.Builder()
      .addInterceptor(HttpLoggingInterceptor { println(it) }.setLevel(HttpLoggingInterceptor.Level.BASIC))
      .addInterceptor(httpTrackingInterceptor)
      .addInterceptor(Interceptor { chain ->
        chain.proceed(chain.request())
          .also { testTimeProvider.advanceTime(123) }
      })
      .build()
  }

  @Test
  fun reportsEventProperly() {
    mockWebServer.enqueue(MockResponse().setResponseCode(201))

    client.newCall(builder().build()).execute()

    val analyticsEvent = recordingAnalytics.events().single()
    assertThat(analyticsEvent.key.name).isEqualTo("http_request")
    assertThat(analyticsEvent.properties["resource.duration"]).isEqualTo(123L)
    assertThat(analyticsEvent.properties["http.method"]).isEqualTo("GET")
    assertThat(analyticsEvent.properties["http.request_content_length"]).isEqualTo(0L)
    assertThat(analyticsEvent.properties["http.response_content_length"]).isEqualTo(0L)
    assertThat(analyticsEvent.properties["http.status_code"]).isEqualTo(201)
    assertThat(analyticsEvent.properties["http.request_id"]).isEqualTo("testId")
    assertThat(analyticsEvent.properties["http.url"] as String)
      .doesNotContain("sensitive").doesNotContain("info").contains("http://localhost:")
  }

  @Test
  fun reportsSize() {
    mockWebServer.enqueue(
      MockResponse()
        .setResponseCode(200)
        .addHeader("Content-Length", 231)
    )

    client.newCall(
      builder()
        .post("bytes".toRequestBody())
        .build()
    )
      .execute()

    val analyticsEvent = recordingAnalytics.events().single()
    assertThat(analyticsEvent.properties["http.method"]).isEqualTo("POST")
    assertThat(analyticsEvent.properties["http.status_code"]).isEqualTo(200)
    assertThat(analyticsEvent.properties["http.request_content_length"]).isEqualTo(5L)
    assertThat(analyticsEvent.properties["http.response_content_length"]).isEqualTo(231L)

    mockWebServer.enqueue(
      MockResponse()
        .setResponseCode(200)
        .setBody("ninebytes")
    )

    client.newCall(
      builder()
        .put("not ten bytes".toRequestBody())
        .addHeader("Content-Length", "10")
        .build()
    ).execute()

    val secondEvent = recordingAnalytics.events().last()
    assertThat(secondEvent.properties["http.method"]).isEqualTo("PUT")
    assertThat(secondEvent.properties["http.status_code"]).isEqualTo(200)
    assertThat(secondEvent.properties["http.request_content_length"]).isEqualTo(10L)
    assertThat(secondEvent.properties["http.response_content_length"]).isEqualTo(9L)
  }

  private fun builder(): Request.Builder {
    return Request.Builder()
      .url(
        mockWebServer.url("/")
          .newBuilder()
          .addQueryParameter("sensitive", "info")
          .build()
      )
      .addHeader("Request-Id", "testId")
      .method("GET", null)
  }
}
