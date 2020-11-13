package com.jraska.github.client

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FirebaseConfigModule {
  @Provides
  @Singleton
  internal fun config(decorations: Set<@JvmSuppressWildcards Config.Decoration>): Config {
    val configProxy = FirebaseConfigProxy(FirebaseRemoteConfig.getInstance())

    configProxy.setupDefaults().fetch()

    var config: Config = configProxy
    decorations.forEach {
      config = it.decorate(config)
    }

    return config
  }
}
