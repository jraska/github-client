package com.jraska.analytics

data class AnalyticsEvent(
  val name: String,
  val properties: Map<String, Any?>
)
