package com.jraska.github.client;

import com.jraska.github.client.http.MockingResponsesHttpComponent;
import com.jraska.github.client.http.HttpComponent;

public class TestUITestApp extends GitHubClientApp {
  @Override protected HttpComponent httpComponent() {
    return MockingResponsesHttpComponent.create();
  }

  @Override protected DaggerAppComponent.Builder componentBuilder() {
    return super.componentBuilder()
      .firebaseModule(new FakeFirebaseModule());
  }
}
