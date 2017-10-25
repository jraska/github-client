package com.jraska.github.client.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jraska.github.client.GitHubClientApp

class PushHandleService : FirebaseMessagingService() {
  override fun onMessageReceived(remoteMessage: RemoteMessage?) {
    val app = application as GitHubClientApp
    val action = RemoteMessageToActionConverter.convert(remoteMessage!!)

    app.pushHandler().handlePush(action)
  }
}
