package com.jraska.github.client;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class NavigationModule {
  @Provides
  public static Navigator provideNavigator(WebLinkLauncher webLinkLauncher, DeepLinkLauncher deepLinkLauncher) {
    return new DeepLinkNavigator(deepLinkLauncher, webLinkLauncher);
  }

  @Provides
  public static DeepLinkLauncher provideDeepLinkLauncher(Activity activity) {
    return new RealDeepLinkLauncher(activity);
  }

  @Provides
  public static WebLinkLauncher webLinkLauncher(Activity activity) {
    return new RealWebLinkLauncher(activity);
  }
}
