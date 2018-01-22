package com.jraska.github.client.logging

import android.util.Log
import com.jraska.github.client.common.DeveloperError
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

class ErrorReportTree @Inject internal constructor(private val crashReporter: CrashReporter) : Timber.Tree() {

  override fun isLoggable(tag: String?, priority: Int): Boolean {
    return priority >= Log.ERROR
  }

  override fun log(priority: Int, tagParam: String?, message: String, errorParam: Throwable?) {
    var tag = tagParam
    var error = errorParam
    if (tag == null) {
      tag = createStackTag()
    }

    if (error == null) {
      error = DeveloperError("Error without exception")
    }

    if (tag != null) {
      crashReporter.report(error, tag + "/" + message)
    } else {
      crashReporter.report(error, message)
    }
  }

  private fun createStackElementTag(element: StackTraceElement): String {
    var tag = element.className
    val m = ANONYMOUS_CLASS.matcher(tag)
    if (m.find()) {
      tag = m.replaceAll("")
    }
    return tag.substring(tag.lastIndexOf('.') + 1)
  }

  private fun createStackTag(): String? {
    val stackTrace = Throwable().stackTrace
    return if (stackTrace.size <= CALL_STACK_INDEX) {
      null
    } else createStackElementTag(stackTrace[CALL_STACK_INDEX])
  }

  companion object {
    private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    private val CALL_STACK_INDEX = 6
  }
}
