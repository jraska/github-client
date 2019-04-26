package com.jraska.github.client.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

internal class PushHandleService : FirebaseMessagingService() {
  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    val action = RemoteMessageToActionConverter.convert(remoteMessage)

    pushHandler().handlePush(action)
  }

  override fun onNewToken(p0: String?) {
    pushHandler().onTokenRefresh()
  }

  private fun pushHandler(): PushHandler {
    val app = application as HasPushHandler
    return app.pushHandler()
  }
}
