package com.jraska.github.client

import com.jraska.github.client.core.android.TopActivityProvider
import dagger.Module
import dagger.Provides

@Module
object ChromeCustomTabsModule {

  @Provides
  fun webLinkLauncher(provider: TopActivityProvider): WebLinkLauncher {
    return ChromeCustomTabsLauncher(provider)
  }
}
