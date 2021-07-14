package com.jraska.github.client.push

import com.google.firebase.messaging.RemoteMessage
import com.jraska.github.client.core.android.ServiceModel
import javax.inject.Inject

internal class PushHandleModelImpl @Inject constructor(
  private val pushHandler: PushHandler,
  private val analytics: PushAnalytics,
  private val tokenSynchronizer: PushTokenSynchronizer
) : PushHandleModel, ServiceModel {
  override fun onMessageReceived(message: RemoteMessage) {
    val action = RemoteMessageToActionConverter.convert(message)

    val pushResult = pushHandler.handlePush(action)

    analytics.onPushHandled(action, pushResult)
  }

  override fun onNewToken(token: String) {
    analytics.onTokenRefresh()
    tokenSynchronizer.synchronizeToken(token)
  }
}
