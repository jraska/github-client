package com.jraska.github.client

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.jraska.github.client.core.android.BaseApp
import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.http.FakeHttpModule
import com.jraska.github.client.users.test.DeepLinkRecordingComponent
import com.jraska.github.client.users.test.FakeDeepLinkRecordingModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

class TestUITestApp : BaseApp() {
  private val decoratedServiceFactory by lazy {
    DecoratedServiceModelFactory(super.serviceModelFactory())
  }

  override val appComponent: TestAppComponent by lazy {
    DaggerTestAppComponent.factory().create(this)
  }

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
@Component(
  modules = [
    SharedModules::class,
    FakeCoreModule::class,
    FakeHttpModule::class,
    FakeDeepLinkRecordingModule::class
  ]
)
interface TestAppComponent : AppComponent, DeepLinkRecordingComponent {
  val config: FakeConfig

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): TestAppComponent
  }
}
