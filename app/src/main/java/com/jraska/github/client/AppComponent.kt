package com.jraska.github.client

import android.content.Context
import com.jraska.github.client.about.AboutModule
import com.jraska.github.client.chrome.ChromeCustomTabsModule
import com.jraska.github.client.config.debug.ConfigDebugModule
import com.jraska.github.client.core.android.AppBaseComponent
import com.jraska.github.client.core.android.CoreAndroidModule
import com.jraska.github.client.http.HttpModule
import com.jraska.github.client.identity.IdentityModule
import com.jraska.github.client.inappupdate.InAppUpdateModule
import com.jraska.github.client.networkstatus.NetworkStatusModule
import com.jraska.github.client.push.PushModule
import com.jraska.github.client.repo.RepoModule
import com.jraska.github.client.settings.SettingsModule
import com.jraska.github.client.shortcuts.ShortcutsModule
import com.jraska.github.client.users.UsersModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(
  modules = [SharedModules::class, ToExchange::class]
)
interface AppComponent : AppBaseComponent {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): AppComponent
  }
}

@Module(
  includes = [
    ConfigDebugModule::class,
    CoreAndroidModule::class,
    ChromeCustomTabsModule::class,
    IdentityModule::class,
    NetworkStatusModule::class,
    InAppUpdateModule::class,
    UsersModule::class,
    RepoModule::class,
    PushModule::class,
    SettingsModule::class,
    AboutModule::class,
    ShortcutsModule::class]
)
object SharedModules

@Module(includes = [FirebaseCoreModule::class, HttpModule::class, CoroutinesModule::class])
object ToExchange
