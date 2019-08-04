package com.jraska.github.client

import androidx.lifecycle.ViewModel
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.core.android.RealDeepLinkLauncher
import com.jraska.github.client.core.android.TopActivityProvider
import com.jraska.github.client.core.android.UriHandlerViewModel
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
  fun bindDeepLinkLauncher(
    topActivityProvider: TopActivityProvider,
    launchers: @JvmSuppressWildcards Set<LinkLauncher>
  ): DeepLinkLauncher {
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
  fun uriHandlerViewModel(viewModel: UriHandlerViewModel): ViewModel {
    return viewModel
  }
}
