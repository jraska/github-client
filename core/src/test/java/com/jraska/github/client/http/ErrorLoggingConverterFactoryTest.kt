package com.jraska.github.client.http

import com.google.gson.annotations.SerializedName
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

  private val memoizeErrorHandler = object : ConvertErrorHandler {
    var lastError: Exception? = null

    override fun onConvertRequestBodyError(exception: Exception) {
      lastError = exception
    }

    override fun onConvertResponseError(exception: Exception) {
      lastError = exception
    }
  }

  @Test
  fun whenDeserializing_thenLogsError() {
    mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("""{name: "someName"}"""))

    try {
      testApi().get().execute()
      throw IllegalStateException("Test shouldn't reach this point")
    } catch (exception: Exception) {
      exception.printStackTrace(System.out)
    }

    assertThat(memoizeErrorHandler.lastError).isNotNull
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

    assertThat(memoizeErrorHandler.lastError).isNotNull
  }

  @Test
  fun doesNothingOnSuccess() {
    mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("""{"name": "someName"}"""))
    testApi().get().execute()

    assertThat(memoizeErrorHandler.lastError).isNull()

    mockWebServer.enqueue(MockResponse().setResponseCode(200))
    testApi().post(ErrorBodyDto("hey")).execute()

    assertThat(memoizeErrorHandler.lastError).isNull()
  }

  private fun testApi() = Retrofit.Builder()
    .baseUrl(mockWebServer.url("/"))
    .addConverterFactory(
      ErrorLoggingConverterFactory(
        GsonConverterFactory.create(),
        memoizeErrorHandler
      )
    )
    .build()
    .create(TestApi::class.java)

  interface TestApi {
    @GET("get")
    fun get(): Call<ResponseDto>

    @POST("errorPost")
    fun post(@Body body: ErrorBodyDto): Call<ResponseBody>
  }

  class ErrorBodyDto(
    @SerializedName("name") val any: Any,
  )

  class ResponseDto(
    @SerializedName("name") val name: String,
  )
}
