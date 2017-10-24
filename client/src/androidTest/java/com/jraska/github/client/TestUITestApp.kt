package com.jraska.github.client

import com.jraska.github.client.http.HttpComponent
import com.jraska.github.client.http.MockingResponsesHttpComponent

class TestUITestApp : GitHubClientApp() {
  override fun httpComponent(): HttpComponent {
    return MockingResponsesHttpComponent.create()
  }

  override fun componentBuilder(): DaggerAppComponent.Builder {
    return super.componentBuilder()
      .firebaseModule(FakeFirebaseModule())
  }
}
