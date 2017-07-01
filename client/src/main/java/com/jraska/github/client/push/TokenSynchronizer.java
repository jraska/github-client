package com.jraska.github.client.push;

import com.google.firebase.iid.FirebaseInstanceId;

final class TokenSynchronizer {
  void synchronizeToken() {
    FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
    String id = instanceId.getId();
    String token = instanceId.getToken();

    // TODO(josef):  
  }
}
