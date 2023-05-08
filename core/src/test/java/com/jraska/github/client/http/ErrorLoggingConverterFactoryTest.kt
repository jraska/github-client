package com.jraska.github.client.http

import com.google.gson.annotations.SerializedName
import com.jraska.github.client.Fakes
import okhttp3.ResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class ErrorLoggingConverterFactoryTest {
  private val mockWebServer = MockWebServer()

  private val eventAnalytics = Fakes.recordingAnalytics()

  @Test
  fun whenDeserializing_thenLogsError() {
    mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("""{name: "someName"}"""))

    try {
      testApi().get().execute()
      throw IllegalStateException("Test shouldn't reach this point")
    } catch (exception: Exception) {
      exception.printStackTrace(System.out)
    }

    val event = eventAnalytics.events().single()
    assertThat(event.name).isEqualTo("error_parsing")
    assertThat(event.properties["http_method"]).isEqualTo("GET")
    assertThat(event.properties["http_path"]).isEqualTo("get/some/long/path")
    assertThat(event.properties["error_type"]).isEqualTo("MalformedJsonException")
    assertThat(event.properties["top_frame_method"]).isEqualTo("syntaxError")
    assertThat(event.properties["message"]).isEqualTo("Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 3 path \$.")
  }

  @Test
  fun whenDeserializingWrongType_thenLogsError() {
    mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("""{"name": []}"""))

    try {
      testApi().get().execute()
      throw IllegalStateException("Test shouldn't reach this point")
    } catch (exception: Exception) {
      exception.printStackTrace(System.out)
    }

    val event = eventAnalytics.events().single()
    assertThat(event.name).isEqualTo("error_parsing")
    assertThat(event.properties["error_type"]).isEqualTo("JsonSyntaxException")
    assertThat(event.properties["dto_type"])
      .isEqualTo("com.jraska.github.client.http.ErrorLoggingConverterFactoryTest\$ResponseDto")
  }

  @Test
  fun whenSerializing_thenLogsError() {
    val cannotBeJson = ErrorLoggingConverterFactoryTest::class

    try {
      testApi().post(ErrorBodyDto(cannotBeJson)).execute()
      throw IllegalStateException("Test shouldn't reach this point")
    } catch (exception: Exception) {
      exception.printStackTrace(System.out)
    }

    val event = eventAnalytics.events().single()
    assertThat(event.name).isEqualTo("error_serializing")
    assertThat(event.properties["http_method"]).isEqualTo("POST")
    assertThat(event.properties["http_path"]).isEqualTo("errorPost/path")
    assertThat(event.properties["dto_type"])
      .isEqualTo("com.jraska.github.client.http.ErrorLoggingConverterFactoryTest\$ErrorBodyDto")
    assertThat(event.properties["message"] as String).endsWith("Forgot to register a type adapter?")
    assertThat(event.properties["value_type"]).isEqualTo("com.jraska.github.client.http.ErrorLoggingConverterFactoryTest.ErrorBodyDto")
  }

  @Test
  fun doesNothingOnSuccess() {
    mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("""{"name": "someName"}"""))
    testApi().get().execute()

    assertThat(eventAnalytics.events()).isEmpty()

    mockWebServer.enqueue(MockResponse().setResponseCode(200))
    testApi().post(ErrorBodyDto("hey")).execute()

    assertThat(eventAnalytics.events()).isEmpty()
  }

  private fun testApi() = Retrofit.Builder()
    .baseUrl(mockWebServer.url("/"))
    .addConverterFactory(
      ErrorLoggingConverterFactory(
        GsonConverterFactory.create(),
        ReportingConvertErrorHandler(eventAnalytics)
      )
    )
    .build()
    .create(TestApi::class.java)

  interface TestApi {
    @GET("get/some/long/path?queryParam=3")
    fun get(): Call<ResponseDto>

    @POST("errorPost/path")
    fun post(@Body body: ErrorBodyDto): Call<ResponseBody>
  }

  class ErrorBodyDto(
    @SerializedName("name") val any: Any,
  )

  class ResponseDto(
    @SerializedName("name") val name: String,
  )
}
