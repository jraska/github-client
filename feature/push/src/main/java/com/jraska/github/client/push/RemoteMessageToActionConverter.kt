package com.jraska.github.client.push

import com.google.firebase.messaging.RemoteMessage

internal object RemoteMessageToActionConverter {
  private const val KEY_ACTION = "action"
  private const val DEFAULT_ACTION = "message_without_data"

  fun convert(remoteMessage: RemoteMessage): PushAction {
    return convert(remoteMessage.data)
  }

  fun convert(remoteMessageData: Map<String, String>): PushAction {
    val action = remoteMessageData[KEY_ACTION] ?: DEFAULT_ACTION

    val properties: Map<String, String>
    if (remoteMessageData.size == 1) {
      properties = emptyMap()
    } else {
      properties = HashMap(remoteMessageData)
      properties.remove(KEY_ACTION)
    }

    return PushAction(action, properties)
  }
}
