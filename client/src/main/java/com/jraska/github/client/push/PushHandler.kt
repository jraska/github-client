package com.jraska.github.client.push

import com.jraska.github.client.Config
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics
import dagger.Lazy
import timber.log.Timber
import javax.inject.Inject

class PushHandler @Inject internal constructor(
  private val eventAnalytics: EventAnalytics,
  private val tokenSynchronizer: PushTokenSynchronizer,
  private val config: Lazy<Config>,
  private val analyticsProperty: Lazy<AnalyticsProperty>) {

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
    return when (action.name) {
      ACTION_REFRESH_CONFIG -> refreshConfig()
      ACTION_CONFIG_VALUE_AS_PROPERTY -> configAsProperty(action)
      ACTION_SET_ANALYTICS_PROPERTY -> setAnalyticsProperty(action)

      else -> false
    }
  }

  private fun setAnalyticsProperty(action: PushAction): Boolean {
    val key = action.parameters["property_key"] ?: return false

    val value = action.parameters["property_value"] ?: return false

    analyticsProperty.get().setUserProperty(key, value)
    return true
  }

  private fun configAsProperty(action: PushAction): Boolean {
    val key = action.parameters["config_key"] ?: return false

    val value = config.get().getString(key)
    analyticsProperty.get().setUserProperty(key, value)
    return true
  }

  private fun refreshConfig(): Boolean {
    config.get().triggerRefresh()
    return true
  }

  internal fun onTokenRefresh() {
    tokenSynchronizer.synchronizeToken()

    val tokenEvent = AnalyticsEvent.create("push_token_refresh")
    eventAnalytics.report(tokenEvent)
  }

  companion object {
    private val ACTION_REFRESH_CONFIG = "refresh_config"
    private val ACTION_CONFIG_VALUE_AS_PROPERTY = "set_config_as_property"
    private val ACTION_SET_ANALYTICS_PROPERTY = "set_analytics_property"
  }
}
