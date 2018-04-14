package com.jraska.github.client

import com.jraska.github.client.http.HttpComponent
import com.jraska.github.client.http.ReplayHttpComponent

class TestUITestApp : GitHubClientApp() {
  override fun httpComponent(): HttpComponent {
    return ReplayHttpComponent.create()
  }

  override fun coreComponent(): CoreComponent {
    return FakeCoreComponent()
  }
}
