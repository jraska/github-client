package com.jraska.github.client.http

import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import java.lang.reflect.Type
import javax.inject.Inject

class ReportingConvertErrorHandler @Inject constructor(
  private val eventAnalytics: EventAnalytics
) : ConvertErrorHandler {

  override fun onConvertRequestBodyError(exception: Exception, type: Type) {
    val requestBodyError = AnalyticsEvent.builder(REQUEST_CONVERT_ERROR)
      .addExceptionProperties(exception, type)
      .build()

    eventAnalytics.report(requestBodyError)
  }

  override fun onConvertResponseError(exception: Exception, type: Type) {
    val responseBodyError = AnalyticsEvent.builder(RESPONSE_CONVERT_ERROR)
      .addExceptionProperties(exception, type)
      .build()

    eventAnalytics.report(responseBodyError)
  }

  private fun AnalyticsEvent.Builder.addExceptionProperties(
    exception: Exception,
    type: Type
  ): AnalyticsEvent.Builder {
    addProperty("dto_type", type.toString().max100End())
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

  companion object {
    private val REQUEST_CONVERT_ERROR =
      AnalyticsEvent.Key("error_serializing", Owner.PERFORMANCE_TEAM)
    private val RESPONSE_CONVERT_ERROR =
      AnalyticsEvent.Key("error_parsing", Owner.PERFORMANCE_TEAM)
  }
}
