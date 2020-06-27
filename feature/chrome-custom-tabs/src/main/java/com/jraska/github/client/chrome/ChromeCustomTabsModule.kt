package com.jraska.github.client.chrome

import android.content.Context
import com.jraska.github.client.WebLinkLauncher
import com.jraska.github.client.core.android.TopActivityProvider
import dagger.Module
import dagger.Provides

@Module
object ChromeCustomTabsModule {

  @Provides
  fun webLinkLauncher(provider: TopActivityProvider, context: Context): WebLinkLauncher {
    return ChromeCustomTabsLauncher(provider, context.packageManager)
  }
}
