package com.jraska.github.client.about.entrance.internal

import com.jraska.github.client.core.android.LinkLauncher
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
object AboutFeatureEntranceModule {
  @JvmStatic
  @Provides
  @IntoSet
  internal fun provideLauncher(launcher: DynamicAboutLinkLauncher): LinkLauncher {
    return launcher
  }
}
