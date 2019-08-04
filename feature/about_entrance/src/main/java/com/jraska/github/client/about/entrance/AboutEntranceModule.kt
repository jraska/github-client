package com.jraska.github.client.about.entrance

import com.jraska.github.client.core.android.LinkLauncher
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
object AboutEntranceModule {

  @JvmStatic
  @Provides
  @IntoSet
  internal fun provideLauncher(): LinkLauncher {
    return DynamicAboutLinkLauncher()
  }
}
