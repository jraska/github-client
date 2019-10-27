package com.jraska.github.client

import com.jraska.github.client.core.android.TopActivityProvider
import com.jraska.github.client.rx.AppSchedulers
import dagger.Module
import dagger.Provides

@Module
object ChromeCustomTabsModule {

  @Provides
  fun webLinkLauncher(provider: TopActivityProvider, appSchedulers: AppSchedulers): WebLinkLauncher {
    return ChromeCustomTabsLauncher(provider, appSchedulers)
  }
}
