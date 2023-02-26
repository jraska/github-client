package com.jraska.github.client.http

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

class ClientThrottlingInterceptorTest {
  private lateinit var client: OkHttpClient
  private val fakeProbabilityRandom = FakeProbabilityRandom()

  private val mockWebServer = MockWebServer()

  @Before
  fun setUp() {
    client = OkHttpClient.Builder()
      .addInterceptor(ClientThrottlingInterceptor(fakeProbabilityRandom))
      .addInterceptor(HttpLoggingInterceptor { println(it) }.setLevel(HttpLoggingInterceptor.Level.BASIC))
      .build()
  }

  private fun executeNextCall(): Response {
    return client.newCall(
      Request.Builder()
        .url(mockWebServer.url("/"))
        .method("GET", null)
        .build()
    ).execute()
  }


  @Test
  fun nothingThrottledWhenAllGood() {
    repeat(100) {
      mockWebServer.enqueue(successResponse())

      val response = executeNextCall()
      assertThat(response.code).isEqualTo(200)
    }
  }

  @Test
  fun whenRequestsRejectedStartsClientRejection() {
    repeat(3) {
      mockWebServer.enqueue(rateLimitedResponse())
      executeNextCall()
    }

    fakeProbabilityRandom.nextRejectionProbability = 0.749
    assertThat(mockWebServer.requestCount).isEqualTo(3)
    mockWebServer.enqueue(successResponse())

    assertThat(executeNextCall().message).startsWith("Client rejected")
    assertThat(mockWebServer.requestCount).isEqualTo(3)

    fakeProbabilityRandom.nextRejectionProbability = 0.75
    assertThat(executeNextCall().code).isEqualTo(200)

    mockWebServer.enqueue(successResponse())
    fakeProbabilityRandom.nextRejectionProbability = 0.4
    assertThat(executeNextCall().code).isEqualTo(200)


    mockWebServer.enqueue(rateLimitedResponse())
    assertThat(executeNextCall().code).isEqualTo(403)
    fakeProbabilityRandom.nextRejectionProbability = 0.28

    mockWebServer.enqueue(successResponse())
    assertThat(executeNextCall().message).startsWith("Client rejected")

    fakeProbabilityRandom.nextRejectionProbability = 0.29
    assertThat(executeNextCall().code).isEqualTo(200)


    fakeProbabilityRandom.nextRejectionProbability = 0.75
    repeat(20) {
      mockWebServer.enqueue(rateLimitedResponse())
      assertThat(executeNextCall().code).isEqualTo(403)
    }

    repeat(10) {
      mockWebServer.enqueue(successResponse())
      assertThat(executeNextCall().code).isEqualTo(200)
    }

    mockWebServer.enqueue(successResponse())
    fakeProbabilityRandom.nextRejectionProbability = 0.28
    assertThat(executeNextCall().message).startsWith("Client rejected")
    fakeProbabilityRandom.nextRejectionProbability = 0.29
    assertThat(executeNextCall().code).isEqualTo(200)
  }

  private fun successResponse() = MockResponse().setResponseCode(200)

  private fun rateLimitedResponse() =
    MockResponse().setResponseCode(403).addHeader("x-ratelimit-remaining:0")

  class FakeProbabilityRandom : Random() {
    var nextRejectionProbability = 0.9

    override fun nextDouble(): Double {
      return nextRejectionProbability
    }
  }
}
