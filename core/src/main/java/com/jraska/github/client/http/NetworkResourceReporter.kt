package com.jraska.github.client.http

import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.analytics.toAnalyticsString
import javax.inject.Inject

class NetworkResourceReporter @Inject constructor(
  private val eventAnalytics: EventAnalytics
) {
  fun report(resource: NetworkResource) {
    val analyticsEvent = AnalyticsEvent.builder(NETWORK_RESOURCE_EVENT)
      .addProperty("http.method", resource.method)
      .addProperty("http.status_code", resource.statusCode)
      .addProperty("http.request_content_length", resource.requestContentLength)
      .addProperty("http.response_content_length", resource.responseContentLength)
      .addProperty("http.url", resource.url.toAnalyticsString())
      .addProperty("resource.duration", resource.durationMs)
      .addProperty("http.request_id", resource.requestId)
      .build()

    eventAnalytics.report(analyticsEvent)
  }

  companion object {
    val NETWORK_RESOURCE_EVENT = AnalyticsEvent.Key("http_request", Owner.PERFORMANCE_TEAM)
  }
}
