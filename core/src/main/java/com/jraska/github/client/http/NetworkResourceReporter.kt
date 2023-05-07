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
      .addProperty("http_method", resource.method)
      .addProperty("http_status_code", resource.statusCode)
      .addProperty("http_request_length", resource.requestContentLength)
      .addProperty("http_response_length", resource.responseContentLength)
      .addProperty("http_url", resource.url.toAnalyticsString())
      .addProperty("resource_duration", resource.durationMs)
      .addProperty("http_request_id", resource.requestId)
      .build()

    eventAnalytics.report(analyticsEvent)
  }

  companion object {
    val NETWORK_RESOURCE_EVENT = AnalyticsEvent.Key("http_request", Owner.PERFORMANCE_TEAM)
  }
}
