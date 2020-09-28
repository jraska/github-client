package com.jraska.github.client.push

import com.google.firebase.messaging.RemoteMessage
import com.jraska.github.client.core.android.ServiceModel

interface PushHandleModel : ServiceModel {
  fun onMessageReceived(message: RemoteMessage)

  fun onNewToken(token: String)
}
