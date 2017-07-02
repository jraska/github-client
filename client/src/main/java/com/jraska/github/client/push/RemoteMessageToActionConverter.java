package com.jraska.github.client.push;

import com.google.firebase.messaging.RemoteMessage;

final class RemoteMessageToActionConverter {
  public static PushAction convert(RemoteMessage remoteMessage) {
    String action = remoteMessage.getData().get(PushAction.KEY_ACTION);

    if (action == null) {
      return PushAction.DEFAULT;
    }

    return PushAction.create(action);
  }
}
