package com.jraska.github.client.users

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.jraska.github.client.CoroutinesModule
import com.jraska.github.client.FakeCoreModule
import com.jraska.github.client.FakeWebLinkModule
import com.jraska.github.client.core.android.CoreAndroidModule
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.http.HttpModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    CoreAndroidModule::class,
    FakeCoreModule::class,
    HttpModule::class,
    FakeWebLinkModule::class,
    CoroutinesModule::class,
    UsersModule::class]
)
interface UsersAppComponent {
  fun onAppCreateActions(): Set<OnAppCreate>

  fun viewModelFactory(): ViewModelProvider.Factory

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): UsersAppComponent
  }
}
