package com.jraska.github.client.http

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.HTTP
import retrofit2.http.OPTIONS
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import java.lang.IllegalArgumentException
import java.lang.reflect.Type

interface ConvertErrorHandler {
  fun onConvertRequestBodyError(value: Any, exception: Exception, methodInfo: MethodInfo)
  fun onConvertResponseError(exception: Exception, methodInfo: MethodInfo)
}

class ErrorLoggingConverterFactory(
  private val delegate: Converter.Factory,
  private val jsonErrorHandler: ConvertErrorHandler,
) : Converter.Factory() {
  override fun responseBodyConverter(
    type: Type,
    annotations: Array<out Annotation>,
    retrofit: Retrofit
  ): Converter<ResponseBody, *>? {
    val delegateConverter = delegate.responseBodyConverter(type, annotations, retrofit)

    if (delegateConverter != null) {
      val methodInfo = methodInfo(annotations, type)
      return ResponseBodyErrorConverter(delegateConverter, jsonErrorHandler, methodInfo)
    } else {
      return null
    }
  }

  override fun requestBodyConverter(
    type: Type,
    parameterAnnotations: Array<out Annotation>,
    methodAnnotations: Array<out Annotation>,
    retrofit: Retrofit
  ): Converter<*, RequestBody>? {
    val delegateConverter =
      delegate.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)

    if (delegateConverter != null) {
      val methodInfo = methodInfo(methodAnnotations, type)
      return RequestBodyErrorConverter(delegateConverter, jsonErrorHandler, methodInfo)
    } else {
      return null
    }
  }

  override fun stringConverter(
    type: Type,
    annotations: Array<out Annotation>,
    retrofit: Retrofit
  ): Converter<*, String>? {
    return delegate.stringConverter(type, annotations, retrofit)
  }

  private fun methodInfo(annotations: Array<out Annotation>, type: Type): MethodInfo {
    annotations.forEach {
      val urlPathWithMethod = urlPathWithMethod(it)
      if(urlPathWithMethod != null) {
        return MethodInfo(urlPathWithMethod.first, urlPathWithMethod.second, type)
      }
    }

    throw IllegalArgumentException("HTTP Method not found in annotations $annotations for $type")
  }

  private fun urlPathWithMethod(annotation: Annotation): Pair<String, String>? {
    return when (annotation) {
      is DELETE -> annotation.value to "DELETE"
      is GET -> annotation.value to "GET"
      is HEAD -> annotation.value to "HEAD"
      is PATCH -> annotation.value to "PATCH"
      is POST -> annotation.value to "POST"
      is PUT -> annotation.value to "PUT"
      is OPTIONS -> annotation.value to "OPTIONS"
      is HTTP -> annotation.path to annotation.method
      else -> null
    }
  }

  private class ResponseBodyErrorConverter<T>(
    private val delegate: Converter<ResponseBody, T>,
    private val jsonErrorHandler: ConvertErrorHandler,
    private val methodInfo: MethodInfo
  ) : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T? {
      try {
        return delegate.convert(value)
      } catch (exception: Exception) {
        jsonErrorHandler.onConvertResponseError(exception, methodInfo)

        throw exception
      }
    }
  }

  private class RequestBodyErrorConverter<T>(
    private val delegate: Converter<T, RequestBody>,
    private val jsonErrorHandler: ConvertErrorHandler,
    private val methodInfo: MethodInfo
  ) : Converter<T, RequestBody> {
    override fun convert(value: T): RequestBody? {
      try {
        return delegate.convert(value)
      } catch (exception: Exception) {
        jsonErrorHandler.onConvertRequestBodyError(value as Any, exception, methodInfo)

        throw exception
      }
    }
  }
}
