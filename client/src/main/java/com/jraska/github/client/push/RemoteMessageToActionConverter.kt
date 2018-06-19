package com.jraska.github.client.push

import com.google.firebase.messaging.RemoteMessage
import java.util.*

internal object RemoteMessageToActionConverter {
  fun convert(remoteMessage: RemoteMessage): PushAction {
    return convert(remoteMessage.data)
  }

  fun convert(remoteMessageData: Map<String, String>): PushAction {
    val action = remoteMessageData[PushAction.KEY_ACTION] ?: return PushAction.DEFAULT

    val properties: Map<String, String>
    if (remoteMessageData.size == 1) {
      properties = emptyMap()
    } else {
      properties = HashMap(remoteMessageData)
      properties.remove(PushAction.KEY_ACTION)
    }

    return PushAction.create(action, properties)
  }
}
