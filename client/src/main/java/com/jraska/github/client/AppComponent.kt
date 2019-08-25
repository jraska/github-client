package com.jraska.github.client

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.jraska.github.client.about.entrance.AboutFeatureEntranceModule
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.dynamicbase.DynamicFeaturesModule
import com.jraska.github.client.http.HttpComponent
import com.jraska.github.client.identity.IdentityModule
import com.jraska.github.client.identity.IdentityProvider
import com.jraska.github.client.push.PushModule
import com.jraska.github.client.settings.entrance.SettingsEntranceModule
import com.jraska.github.client.shortcuts.ShortcutsModule
import com.jraska.github.client.users.UsersModule
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent

@PerApp
@Component(
  modules = [
    AppModule::class,
    NavigationModule::class,
    DynamicFeaturesModule::class,
    IdentityModule::class,
    UsersModule::class,
    PushModule::class,
    SettingsEntranceModule::class,
    AboutFeatureEntranceModule::class,
    ShortcutsModule::class],
  dependencies = [
    HttpComponentDelegate::class,
    CoreComponentDelegate::class
  ]
)
interface AppComponent {

  fun onAppCreateActions(): Set<OnAppCreate>

  fun serviceModelFactory(): ServiceModel.Factory

  fun viewModelFactory(): ViewModelProvider.Factory

  fun dynamicFeaturesComponent(): DynamicFeaturesComponent

  @Component.Builder
  interface Builder {
    fun build(): AppComponent

    fun coreComponent(delegate: CoreComponentDelegate): Builder
    fun httpComponent(module: HttpComponentDelegate): Builder

    @BindsInstance
    fun appContext(context: Context): Builder
  }
}

@Subcomponent
interface DynamicFeaturesComponent {
  fun navigator(): Navigator

  fun identityProvider(): IdentityProvider

  fun eventAnalytics(): EventAnalytics
}

class HttpComponentDelegate(private val httpComponent: HttpComponent) : HttpComponent by httpComponent

class CoreComponentDelegate(private val coreComponent: CoreComponent) : CoreComponent by coreComponent
