package com.jraska.github.client.push

import com.google.firebase.messaging.RemoteMessage
import com.jraska.github.client.core.android.ServiceModel
import javax.inject.Inject

internal class PushHandleModel @Inject constructor(
  private val pushHandler: PushHandler
) : ServiceModel {
  fun onMessageReceived(remoteMessage: RemoteMessage) {
    val action = RemoteMessageToActionConverter.convert(remoteMessage)
    pushHandler.handlePush(action)
  }

  fun onNewToken(token: String) {
    pushHandler.onTokenRefresh(token)
  }
}
