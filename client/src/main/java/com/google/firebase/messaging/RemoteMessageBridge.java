package com.google.firebase.messaging;

import android.os.Bundle;

/**
 * I believe this is better then copying internal implementations of it.
 */
public final class RemoteMessageBridge {
  private RemoteMessageBridge() {
  }

  public static RemoteMessage create(Bundle bundle) {
    return new RemoteMessage(bundle);
  }
}
