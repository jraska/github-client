package com.jraska.github.client.navigation.deeplink

import com.jraska.github.client.DeepLinkLauncher
import com.jraska.github.client.WebLinkLauncher
import com.jraska.github.client.navigation.Navigator
import com.jraska.github.client.navigation.deeplink.internal.DeepLinkNavigator
import dagger.Module
import dagger.Provides

@Module
object DeepLinkNavigationModule {
  @JvmStatic
  @Provides
  fun provideNavigator(webLinkLauncher: WebLinkLauncher, deepLinkLauncher: DeepLinkLauncher): Navigator {
    return DeepLinkNavigator(deepLinkLauncher, webLinkLauncher)
  }
}
