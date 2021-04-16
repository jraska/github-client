package com.jraska.github.client

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.jraska.github.client.core.android.HasServiceModelFactory
import com.jraska.github.client.core.android.HasViewModelFactory
import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.http.ReplayHttpModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

class TestUITestApp : GitHubClientApp(), HasViewModelFactory, HasServiceModelFactory {
  private val decoratedServiceFactory by lazy {
    DecoratedServiceModelFactory(super.serviceModelFactory())
  }

  val appComponent: TestAppComponent by lazy {
    DaggerTestAppComponent.factory().create(this)
  }

  override fun createAppComponent() = appComponent

  override fun serviceModelFactory(): ServiceModel.Factory {
    return decoratedServiceFactory
  }

  companion object {
    fun get(): TestUITestApp {
      return InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestUITestApp
    }
  }
}

@Singleton
@Component(modules = [SharedModules::class, FakeCoreModule::class, ReplayHttpModule::class])
interface TestAppComponent : AppComponent {
  val config: FakeConfig

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): TestAppComponent
  }
}
