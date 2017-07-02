package com.jraska.github.client.push;

import com.google.firebase.messaging.RemoteMessage;
import com.jraska.github.client.common.Preconditions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class RemoteMessageToActionConverter {
  public static PushAction convert(RemoteMessage remoteMessage) {
    return convert(remoteMessage.getData());
  }

  public static PushAction convert(Map<String, String> remoteMessageData) {
    Preconditions.argNotNull(remoteMessageData);

    String action = remoteMessageData.get(PushAction.KEY_ACTION);
    if (action == null) {
      return PushAction.DEFAULT;
    }

    Map<String, String> properties;
    if (remoteMessageData.size() == 1) {
      properties = Collections.emptyMap();
    } else {
      properties = new HashMap<>(remoteMessageData);
      properties.remove(PushAction.KEY_ACTION);
    }

    return PushAction.create(action, properties);
  }
}
