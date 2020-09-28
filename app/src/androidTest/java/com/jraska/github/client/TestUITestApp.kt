package com.jraska.github.client

import androidx.test.platform.app.InstrumentationRegistry
import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.http.ReplayHttpComponent

class TestUITestApp : GitHubClientApp() {
  val coreComponent = FakeCoreComponent()
  val decoratedServiceFactory by lazy {
    DecoratedServiceModelFactory(super.serviceModelFactory())
  }

  override fun serviceModelFactory(): ServiceModel.Factory {
    return decoratedServiceFactory
  }

  override fun retrofit(): HasRetrofit {
    return ReplayHttpComponent.create()
  }

  override fun coreComponent(): CoreComponent {
    return coreComponent
  }

  companion object {
    fun get(): TestUITestApp {
      return InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestUITestApp
    }
  }
}
