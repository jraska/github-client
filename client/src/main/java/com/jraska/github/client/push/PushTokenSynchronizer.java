package com.jraska.github.client.push;

import android.os.Build;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jraska.github.client.time.DateTimeProvider;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

final class PushTokenSynchronizer {
  private final FirebaseDatabase database;
  private final DateTimeProvider dateTimeProvider;

  @Inject PushTokenSynchronizer(FirebaseDatabase database, DateTimeProvider dateTimeProvider) {
    this.database = database;
    this.dateTimeProvider = dateTimeProvider;
  }

  void synchronizeToken() {
    FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
    String id = instanceId.getId();
    String token = instanceId.getToken();

    Timber.d("Id: %s, Token: %s", id, token);

    if (id == null || token == null) {
      return;
    }

    Map<String, Object> map = new HashMap<>();
    map.put("date", dateTimeProvider.now().toString());

    map.put("push_token", token);
    map.put("android_os", Build.VERSION.RELEASE);
    map.put("manufacturer", Build.BRAND);
    map.put("model", Build.MODEL);

    DatabaseReference tokenReference = database.getReference("devices/" + id);
    tokenReference.setValue(map);
  }
}
