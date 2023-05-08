package com.jraska.github.client.http

import android.os.Build.VERSION_CODES.P
import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import javax.inject.Inject

class ReportingConvertErrorHandler @Inject constructor(
  private val eventAnalytics: EventAnalytics
) : ConvertErrorHandler {

  override fun onConvertRequestBodyError(value: Any, exception: Exception, methodInfo: MethodInfo) {
    val requestBodyError = AnalyticsEvent.builder(REQUEST_CONVERT_ERROR)
      .addExceptionProperties(exception, methodInfo)
      .addProperty("value_type", value::class.qualifiedName.max100End())
      .build()

    eventAnalytics.report(requestBodyError)
  }

  override fun onConvertResponseError(exception: Exception, methodInfo: MethodInfo) {
    val responseBodyError = AnalyticsEvent.builder(RESPONSE_CONVERT_ERROR)
      .addExceptionProperties(exception, methodInfo)
      .build()

    eventAnalytics.report(responseBodyError)
  }

  private fun AnalyticsEvent.Builder.addExceptionProperties(
    exception: Exception,
    methodInfo: MethodInfo,
  ): AnalyticsEvent.Builder {
    val dtoType = if(methodInfo.dtoType is Class<*>) {
      methodInfo.dtoType.name
    } else {
      methodInfo.dtoType.toString()
    }

    addProperty("dto_type", dtoType.max100End())
    addProperty("http_method", methodInfo.method)
    addProperty("http_path", methodInfo.httpPath.omitQueryParams().max100End())
    addProperty("message", exception.message.max100End())
    addProperty("error_type", exception::class.simpleName.max100End())

    val topFrame = exception.stackTrace.firstOrNull() ?: return this
    addProperty("top_frame_method", topFrame.methodName.max100End())
    addProperty("top_frame_file", topFrame.fileName.max100End())
    addProperty("top_frame_line_number", topFrame.lineNumber)
    addProperty("top_frame_class", topFrame.className.max100End())

    return this
  }

  private fun String?.max100End(): String {
    if (this == null) return ""

    if (length <= 100) return this

    return substring(length - 100)
  }

  private fun String.omitQueryParams(): String {
    val questionIndex: Int = indexOf('?')

    if(questionIndex > 0) {
      return substring(0, questionIndex)
    } else {
      return this
    }
  }

  companion object {
    private val REQUEST_CONVERT_ERROR =
      AnalyticsEvent.Key("error_serializing", Owner.PERFORMANCE_TEAM)
    private val RESPONSE_CONVERT_ERROR =
      AnalyticsEvent.Key("error_parsing", Owner.PERFORMANCE_TEAM)
  }
}
