package com.jraska.github.client;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class NavigationModule {
  @Provides
  public static Navigator provideNavigator(WebLinkLauncher webLinkLauncher, DeepLinkLauncher deepLinkLauncher) {
    return new DeepLinkNavigator(deepLinkLauncher, webLinkLauncher);
  }

  @Provides
  public static DeepLinkLauncher provideDeepLinkLauncher(TopActivityProvider provider) {
    return new RealDeepLinkLauncher(provider);
  }

  @Provides
  public static WebLinkLauncher webLinkLauncher(TopActivityProvider provider) {
    return new ChromeCustomTabsLauncher(provider);
  }
}
