package com.jraska.github.client.push

import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.common.BooleanResult
import com.jraska.github.client.identity.IdentityProvider
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class PushHandler @Inject internal constructor(
  private val eventAnalytics: EventAnalytics,
  private val tokenSynchronizer: PushTokenSynchronizer,
  private val identityProvider: IdentityProvider,
  private val pushCommands: Map<String, @JvmSuppressWildcards Provider<PushActionCommand>>
) {

  internal fun handlePush(action: PushAction) {
    Timber.v("Push received action: %s", action.name)

    val result = handleInternal(action)

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

  private fun handleInternal(action: PushAction): BooleanResult {
    val actionCommand = pushCommands[action.name] ?: return BooleanResult.FAILURE

    return actionCommand.get().execute(action)
  }

  internal fun onTokenRefresh(token: String) {
    tokenSynchronizer.synchronizeToken(token)

    val tokenEvent = AnalyticsEvent.builder("push_token_refresh")
      .addProperty("session_id", identityProvider.session().id.toString())
      .build()
    eventAnalytics.report(tokenEvent)
  }
}
