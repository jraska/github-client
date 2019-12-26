package com.jraska.github.client.push

import com.google.firebase.messaging.RemoteMessage
import com.jraska.github.client.core.android.ServiceModel
import javax.inject.Inject

internal class PushHandleModel @Inject constructor(
  private val pushHandler: PushHandler,
  private val analytics: PushAnalytics,
  private val tokenSynchronizer: PushTokenSynchronizer
) : ServiceModel {
  fun onMessageReceived(remoteMessage: RemoteMessage) {
    val action = RemoteMessageToActionConverter.convert(remoteMessage)

    val pushResult = pushHandler.handlePush(action)

    analytics.onPushHandled(action, pushResult)
  }

  fun onNewToken(token: String) {
    analytics.onTokenRefresh()
    tokenSynchronizer.onTokenRefresh(token)
  }
}
