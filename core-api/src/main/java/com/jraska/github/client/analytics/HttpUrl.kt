package com.jraska.github.client.analytics

import okhttp3.HttpUrl

fun HttpUrl.toAnalyticsString(): String {
  val builder = newBuilder()
  for (parameterName in queryParameterNames) {
    builder.removeAllQueryParameters(parameterName)
  }

  return builder.build().toString()
}
