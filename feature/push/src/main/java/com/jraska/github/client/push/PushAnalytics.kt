package com.jraska.github.client.push

import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.identity.IdentityProvider
import javax.inject.Inject

internal class PushAnalytics @Inject constructor(
  private val identityProvider: IdentityProvider,
  private val eventAnalytics: EventAnalytics
) {
  fun onTokenRefresh() {
    val tokenEvent = AnalyticsEvent.builder(ANALYTICS_PUSH_TOKEN_REFRESH)
      .addProperty("session_id", identityProvider.session().id.toString())
      .build()
    eventAnalytics.report(tokenEvent)
  }

  fun onPushHandled(action: PushAction, result: PushExecuteResult) {
    if (result == PushExecuteResult.SUCCESS) {
      val pushHandled = AnalyticsEvent.builder(ANALYTICS_PUSH_HANDLED)
        .addProperty("push_action", action.name)
        .build()
      eventAnalytics.report(pushHandled)
    } else {
      val pushHandled = AnalyticsEvent.builder(ANALYTICS_PUSH_NOT_HANDLED)
        .addProperty("push_action", action.name)
        .build()
      eventAnalytics.report(pushHandled)
    }
  }

  companion object {
    val ANALYTICS_PUSH_TOKEN_REFRESH = AnalyticsEvent.Key("push_token_refresh", Owner.CORE_TEAM)
    val ANALYTICS_PUSH_HANDLED = AnalyticsEvent.Key("push_handled", Owner.CORE_TEAM)
    val ANALYTICS_PUSH_NOT_HANDLED = AnalyticsEvent.Key("push_not_handled", Owner.CORE_TEAM)
  }
}
