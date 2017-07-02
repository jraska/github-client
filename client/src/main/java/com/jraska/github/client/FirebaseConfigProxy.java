package com.jraska.github.client;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.Date;

import timber.log.Timber;

final class FirebaseConfigProxy implements Config {
  private static final long TWELVE_HOURS = 12 * 60 * 60;
  private static final long IGNORE_CACHE = 1; // don't ever put zero! That is ignored and cache is used

  private final FirebaseRemoteConfig config;
  private final OnCompleteListener<Void> onFetchCompleteListener;

  FirebaseConfigProxy(FirebaseRemoteConfig config) {
    this.config = config;
    onFetchCompleteListener = (task) -> {
      config.activateFetched();
      Timber.d("Config fetch complete. last fetch: %s",
        new Date(config.getInfo().getFetchTimeMillis()));
    };
  }

  @Override public boolean getBoolean(String key) {
    return config.getBoolean(key);
  }

  @Override public long getLong(String key) {
    return config.getLong(key);
  }

  @Override public String getString(String key) {
    return config.getString(key);
  }

  @Override public void triggerRefresh() {
    config.fetch(IGNORE_CACHE).addOnCompleteListener(onFetchCompleteListener);
  }

  FirebaseConfigProxy fetch() {
    config.fetch(TWELVE_HOURS).addOnCompleteListener(onFetchCompleteListener);
    return this;
  }

  FirebaseConfigProxy setupDefaults() {
    config.setDefaults(R.xml.config_defaults);
    return this;
  }
}
