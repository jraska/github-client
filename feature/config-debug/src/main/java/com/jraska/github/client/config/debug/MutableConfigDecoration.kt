package com.jraska.github.client.config.debug

import com.jraska.github.client.Config

class MutableConfigDecoration : Config.Decoration {
  lateinit var mutableConfig: MutableConfig

  override fun decorate(config: Config): Config {
    return MutableConfig(config)
      .also { mutableConfig = it }
  }
}
