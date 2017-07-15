package com.jraska.github.client;

import android.annotation.SuppressLint;

import com.jraska.github.client.http.FakeHttpComponent;
import com.jraska.github.client.http.HttpComponent;

@SuppressLint("Registered") // robolectric.properties
public final class TestGitHubClientApp extends GitHubClientApp {
  @Override protected HttpComponent httpComponent() {
    return FakeHttpComponent.create();
  }

  @Override protected DaggerAppComponent.Builder componentBuilder() {
    return super.componentBuilder()
      .firebaseModule(new FakeFirebaseModule());
  }

  @Override void initThreeTen() {
    // we are running on desktop
  }
}
