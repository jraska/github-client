package com.jraska.github.client.chrome

import com.jraska.github.client.WebLinkLauncher
import dagger.Binds
import dagger.Module

@Module
abstract class ChromeCustomTabsModule {

  @Binds
  internal abstract fun webLinkLauncher(launcher: ChromeCustomTabsLauncher): WebLinkLauncher
}
