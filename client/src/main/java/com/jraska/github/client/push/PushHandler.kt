package com.jraska.github.client.push

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.NotificationCompat
import com.jraska.github.client.Config
import com.jraska.github.client.NotificationSetup
import com.jraska.github.client.R
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.ui.UriHandlerActivity
import dagger.Lazy
import timber.log.Timber
import javax.inject.Inject

class PushHandler @Inject internal constructor(
  private val eventAnalytics: EventAnalytics,
  private val tokenSynchronizer: PushTokenSynchronizer,
  private val config: Lazy<Config>,
  private val analyticsProperty: Lazy<AnalyticsProperty>,
  private val context: Context,
  private val notificationManager: NotificationManager) {

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
      ACTION_NOTIFICATION -> showNotification(action)

      else -> false
    }
  }

  private fun showNotification(action: PushAction): Boolean {
    val title = action.parameters["title"] ?: return false
    val message = action.parameters["message"] ?: return false
    val deepLink = action.parameters["clickDeepLink"] ?: return false

    val intent = Intent(context, UriHandlerActivity::class.java)
    intent.data = Uri.parse(deepLink)

    val linkContentIntent = PendingIntent.getActivity(context, 0, intent, 0)

    val notification = NotificationCompat.Builder(context, NotificationSetup.PUSH_CHANNEL_ID)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentTitle(title)
      .setContentText(message)
      .setContentIntent(linkContentIntent)
      .setAutoCancel(true)
      .build()

    notificationManager.notify(PUSH_NOTIFICATION_ID, notification)
    return true
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
    private const val ACTION_REFRESH_CONFIG = "refresh_config"
    private const val ACTION_CONFIG_VALUE_AS_PROPERTY = "set_config_as_property"
    private const val ACTION_SET_ANALYTICS_PROPERTY = "set_analytics_property"
    private const val ACTION_NOTIFICATION = "notification"

    private const val PUSH_NOTIFICATION_ID: Int = 1
  }
}
