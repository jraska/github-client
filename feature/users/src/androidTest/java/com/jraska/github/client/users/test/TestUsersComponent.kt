package com.jraska.github.client.users.test

import android.content.Context
import com.jraska.github.client.FakeCoreModule
import com.jraska.github.client.FakeWebLinkModule
import com.jraska.github.client.android.test.FakeAndroidCoreModule
import com.jraska.github.client.core.android.AppBaseComponent
import com.jraska.github.client.core.android.CoreAndroidModule
import com.jraska.github.client.http.FakeHttpModule
import com.jraska.github.client.http.HttpTest
import com.jraska.github.client.users.UsersModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    FakeCoreModule::class,
    FakeAndroidCoreModule::class,
    FakeHttpModule::class,
    FakeWebLinkModule::class,
    FakeDeepLinkRecordingModule::class,
    UsersModule::class,
    CoreAndroidModule::class
  ]
)
interface TestUsersComponent : AppBaseComponent, DeepLinkRecordingComponent {
  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): TestUsersComponent
  }
}
