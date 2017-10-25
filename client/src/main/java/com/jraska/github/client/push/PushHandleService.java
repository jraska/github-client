package com.jraska.github.client.push;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jraska.github.client.GitHubClientApp;

public final class PushHandleService extends FirebaseMessagingService {
  @Override public void onMessageReceived(RemoteMessage remoteMessage) {
    GitHubClientApp app = ((GitHubClientApp) getApplication());
    PushAction action = RemoteMessageToActionConverter.INSTANCE.convert(remoteMessage);

    app.pushHandler().handlePush(action);
  }
}
