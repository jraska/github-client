package com.jraska.github.client;

import android.arch.lifecycle.ViewModel;

import com.jraska.github.client.analytics.EventAnalytics;
import com.jraska.github.client.ui.ShortcutHandlerModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

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

  @Provides
  @IntoMap
  @ClassKey(UriHandlerViewModel.class)
  public static ViewModel uriHandlerViewModel(DeepLinkHandler deepLinkHandler) {
    return new UriHandlerViewModel(deepLinkHandler);
  }

  @Provides
  @IntoMap
  @ClassKey(ShortcutHandlerModel.class)
  public static ViewModel uriShortcutViewModel(DeepLinkLauncher deepLinkHandler,
                                               EventAnalytics eventAnalytics) {
    return new ShortcutHandlerModel(deepLinkHandler, eventAnalytics);
  }
}
