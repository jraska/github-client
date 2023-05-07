package com.jraska.github.client.http

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

interface ConvertErrorHandler {
  fun onConvertRequestBodyError(exception: Exception, type: Type)
  fun onConvertResponseError(exception: Exception, type: Type)
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
      return ResponseBodyErrorConverter(delegateConverter, jsonErrorHandler, type)
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
      return RequestBodyErrorConverter(delegateConverter, jsonErrorHandler, type)
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

  private class ResponseBodyErrorConverter<T>(
    private val delegate: Converter<ResponseBody, T>,
    private val jsonErrorHandler: ConvertErrorHandler,
    private val type: Type
  ) : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T? {
      try {
        return delegate.convert(value)
      } catch (exception: Exception) {
        jsonErrorHandler.onConvertResponseError(exception, type)

        throw exception
      }
    }
  }

  private class RequestBodyErrorConverter<T>(
    private val delegate: Converter<T, RequestBody>,
    private val jsonErrorHandler: ConvertErrorHandler,
    private val type: Type
  ) : Converter<T, RequestBody> {
    override fun convert(value: T): RequestBody? {
      try {
        return delegate.convert(value)
      } catch (exception: Exception) {
        jsonErrorHandler.onConvertRequestBodyError(exception, type)

        throw exception
      }
    }
  }
}
