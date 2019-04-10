package com.jraska.github.client.analytics

import okhttp3.HttpUrl

fun HttpUrl.toAnalyticsString(): String {
  val queryNames = queryParameterNames()

  val builder = newBuilder()
  for (parameterName in queryNames) {
    builder.removeAllQueryParameters(parameterName)
  }

  return builder.build().toString()
}
