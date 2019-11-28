package com.jraska.github.client

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.jraska.github.client.about.entrance.AboutFeatureEntranceModule
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.core.android.CoreAndroidModule
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.dynamicbase.DynamicFeaturesModule
import com.jraska.github.client.identity.IdentityModule
import com.jraska.github.client.identity.IdentityProvider
import com.jraska.github.client.navigation.Navigator
import com.jraska.github.client.navigation.deeplink.DeepLinkNavigationModule
import com.jraska.github.client.networkstatus.NetworkStatusModule
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
    CoreAndroidModule::class,
    ChromeCustomTabsModule::class,
    DynamicFeaturesModule::class,
    DeepLinkNavigationModule::class,
    IdentityModule::class,
    NetworkStatusModule::class,
    UsersModule::class,
    PushModule::class,
    SettingsEntranceModule::class,
    AboutFeatureEntranceModule::class,
    ShortcutsModule::class],
  dependencies = [
    HasRetrofit::class,
    CoreComponent::class
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

    fun coreComponent(coreComponent: CoreComponent): Builder
    fun httpComponent(retrofit: HasRetrofit): Builder

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
