package com.jraska.github.client.push

import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.common.BooleanResult
import com.jraska.github.client.identity.IdentityProvider
import javax.inject.Inject

internal class PushAnalytics @Inject constructor(
  private val identityProvider: IdentityProvider,
  private val eventAnalytics: EventAnalytics
) {
  fun onTokenRefresh() {
    val tokenEvent = AnalyticsEvent.builder("push_token_refresh")
      .addProperty("session_id", identityProvider.session().id.toString())
      .build()
    eventAnalytics.report(tokenEvent)
  }

  fun onPushHandled(action: PushAction, result: BooleanResult) {
    if (result == BooleanResult.SUCCESS) {
      val pushHandled = AnalyticsEvent.builder("push_handled")
        .addProperty("push_action", action.name)
        .build()
      eventAnalytics.report(pushHandled)
    } else {
      val pushHandled = AnalyticsEvent.builder("push_not_handled")
        .addProperty("push_action", action.name)
        .build()
      eventAnalytics.report(pushHandled)
    }
  }
}
