package com.jraska.github.client.config.debug.ui

import com.jraska.github.client.config.debug.MutableConfigSetup
import com.jraska.github.client.config.debug.MutableConfigType
import javax.inject.Inject

internal class EpoxyMutableConfigsRowProvider @Inject constructor(
  private val setupsSet: Set<@JvmSuppressWildcards MutableConfigSetup>
) : ConfigRowModelProvider {
  override fun epoxyModels(): List<Any> {
    return setupsSet.flatMap {
      it.mutableConfigs().map { configDef ->
        when (configDef.type) {
          MutableConfigType.BOOLEAN -> BooleanConfigModel(configDef)
          MutableConfigType.LONG -> LongConfigModel(configDef)
          MutableConfigType.STRING -> StringConfigModel(configDef)
        }
      }
    }
  }
}
