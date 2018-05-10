package com.jraska.github.client.push

import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class PushHandler @Inject internal constructor(
  private val eventAnalytics: EventAnalytics,
  private val tokenSynchronizer: PushTokenSynchronizer,
  private val pushCommands: Map<String, @JvmSuppressWildcards Provider<PushActionCommand>>) {


  internal fun handlePush(action: PushAction) {
    Timber.v("Push received action: %s", action.name)

    val handled = handleInternal(action)

    if (handled) {
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

  private fun handleInternal(action: PushAction): Boolean {
    val actionCommand = pushCommands.get(action.name) ?: return false

    return actionCommand.get().execute(action)
  }

  internal fun onTokenRefresh() {
    tokenSynchronizer.synchronizeToken()

    val tokenEvent = AnalyticsEvent.create("push_token_refresh")
    eventAnalytics.report(tokenEvent)
  }
}
