package com.jraska.github.client.push;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

import timber.log.Timber;

final class PushTokenSynchronizer {
  private final FirebaseDatabase database;

  @Inject PushTokenSynchronizer(FirebaseDatabase database) {
    this.database = database;
  }

  void synchronizeToken() {
    FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
    String id = instanceId.getId();
    String token = instanceId.getToken();

    Timber.d("Id: %s, Token: %s", id, token);

    if (id == null || token == null) {
      return;
    }

    DatabaseReference tokenReference = database.getReference("devices/" + id + "/push_token");
    tokenReference.setValue(token);
  }
}
