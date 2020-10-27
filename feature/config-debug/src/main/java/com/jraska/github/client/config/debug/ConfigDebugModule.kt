package com.jraska.github.client.config.debug

import com.jraska.github.client.Config
import com.jraska.github.client.config.debug.ui.ConfigRowModelProvider
import com.jraska.github.client.config.debug.ui.EpoxyMutableConfigsRowProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ConfigDebugModule {

  @Provides
  internal fun bindRowProvider(provider: EpoxyMutableConfigsRowProvider): ConfigRowModelProvider = provider

  @Provides
  internal fun bindDecoration(decoration: MutableConfigDecoration): Config.Decoration = decoration

  @Provides
  @Singleton
  internal fun theDecoration() = MutableConfigDecoration()
}
