package com.jraska.github.client

import androidx.lifecycle.ViewModel

import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.ui.ShortcutHandlerModel

import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
object NavigationModule {
  @JvmStatic
  @Provides
  fun provideNavigator(webLinkLauncher: WebLinkLauncher, deepLinkLauncher: DeepLinkLauncher): Navigator {
    return DeepLinkNavigator(deepLinkLauncher, webLinkLauncher)
  }

  @JvmStatic
  @Provides
  fun provideDeepLinkLauncher(provider: TopActivityProvider): DeepLinkLauncher {
    return RealDeepLinkLauncher(provider)
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
}
