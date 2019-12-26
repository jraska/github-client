package com.jraska.github.client.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jraska.github.client.core.android.serviceModel

internal class PushHandleService : FirebaseMessagingService() {
  private val model: PushHandleModel by lazy { serviceModel(PushHandleModel::class.java) }

  override fun onMessageReceived(message: RemoteMessage) = model.onMessageReceived(message)

  override fun onNewToken(token: String) = model.onNewToken(token)
}
