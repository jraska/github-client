package com.jraska.github.client.config.debug

import com.jraska.github.client.Config
import com.jraska.github.client.Owner
import com.jraska.github.client.config.MutableConfigDef
import com.jraska.github.client.config.MutableConfigSetup
import com.jraska.github.client.config.MutableConfigType
import com.jraska.github.client.config.debug.ui.ConfigRowModelProvider
import com.jraska.github.client.config.debug.ui.EpoxyMutableConfigsRowProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
class ConfigDebugModule {

  private lateinit var mutableConfig: MutableConfig

  @Provides
  internal fun bindRowProvider(provider: EpoxyMutableConfigsRowProvider): ConfigRowModelProvider = provider

  @Provides
  internal fun mutableConfig() = mutableConfig

  @Provides
  @IntoSet
  internal fun bindDecoration(): Config.Decoration {
    return object : Config.Decoration {
      override fun decorate(originalConfig: Config): Config {
        return MutableConfig(originalConfig).also { mutableConfig = it }
      }
    }
  }

  @Provides
  @IntoSet
  internal fun mutableConfigs(): MutableConfigSetup {
    return object : MutableConfigSetup {
      override fun mutableConfigs(): List<MutableConfigDef> {
        return listOf(
          MutableConfigDef(
            Config.Key("test_boolean_key", Owner.CORE_TEAM),
            MutableConfigType.BOOLEAN,
            emptyList()
          )
        )
      }
    }
  }
}
