package com.jraska.github.client.push;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;

import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.RemoteMessageBridge;

public final class PushIntentObserver implements LifecycleObserver {
  private final PushHandler pushHandler;

  public PushIntentObserver(PushHandler pushHandler) {
    this.pushHandler = pushHandler;
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  public void onCreate(LifecycleOwner owner) {
    if (!(owner instanceof Activity)) {
      return;
    }

    Activity activity = (Activity) owner;
    Intent intent = activity.getIntent();
    if (isPush(intent)) {
      RemoteMessage message = RemoteMessageBridge.INSTANCE.create(intent.getExtras());
      PushAction action = RemoteMessageToActionConverter.INSTANCE.convert(message);

      pushHandler.handlePush(action);
    }
  }

  private boolean isPush(Intent intent) {
    if (!Intent.ACTION_MAIN.equals(intent.getAction()) || intent.getExtras() == null) {
      return false;
    }

    for (String key : intent.getExtras().keySet()) {
      if (key.startsWith("google.sent_time")) {
        return true;
      }

      if (key.startsWith("google.to")) {
        return true;
      }

      if (key.startsWith("gcm.")) {
        return true;
      }
    }

    return false;
  }
}
