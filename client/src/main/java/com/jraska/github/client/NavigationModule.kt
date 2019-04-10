package com.jraska.github.client

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.core.android.RealDeepLinkLauncher
import com.jraska.github.client.core.android.TopActivityProvider
import com.jraska.github.client.core.android.UriHandlerViewModel
import com.jraska.github.client.ui.ShortcutHandlerModel
import com.jraska.github.client.ui.UsersActivity
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import okhttp3.HttpUrl

@Module
object NavigationModule {
  @JvmStatic
  @Provides
  fun provideNavigator(webLinkLauncher: WebLinkLauncher, deepLinkLauncher: DeepLinkLauncher): Navigator {
    return DeepLinkNavigator(deepLinkLauncher, webLinkLauncher)
  }

  @JvmStatic
  @Provides
  fun bindDeepLinkLauncher(
    topActivityProvider: TopActivityProvider,
    launchers: @JvmSuppressWildcards Set<LinkLauncher>): DeepLinkLauncher {
    return RealDeepLinkLauncher.create(topActivityProvider, launchers)
  }

  @JvmStatic
  @Provides
  fun webLinkLauncher(provider: TopActivityProvider): WebLinkLauncher {
    return ChromeCustomTabsLauncher(provider)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(UriHandlerViewModel::class)
  fun uriHandlerViewModel(deepLinkHandler: DeepLinkHandler): ViewModel {
    return UriHandlerViewModel(deepLinkHandler)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(ShortcutHandlerModel::class)
  fun uriShortcutViewModel(
    deepLinkHandler: DeepLinkLauncher,
    eventAnalytics: EventAnalytics
  ): ViewModel {
    return ShortcutHandlerModel(deepLinkHandler, eventAnalytics)
  }

  @JvmStatic
  @Provides
  @IntoSet
  fun provideUsersPathLauncher(): LinkLauncher {
    return UsersPathLauncher()
  }

  @JvmStatic
  @Provides
  @IntoSet
  fun provideUsersListLauncher(): LinkLauncher {
    return object : LinkLauncher {
      override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
        return if ("/users" == deepLink.encodedPath()) {
          UsersActivity.start(inActivity)
          LinkLauncher.Result.LAUNCHED
        } else {
          LinkLauncher.Result.NOT_LAUNCHED
        }
      }

      override fun priority(): LinkLauncher.Priority {
        return LinkLauncher.Priority.EXACT_MATCH
      }
    }
  }
}
