package com.jraska.github.client.config.debug.ui

import com.jraska.github.client.config.MutableConfigSetup
import com.jraska.github.client.config.MutableConfigType
import com.jraska.github.client.config.debug.MutableConfig
import javax.inject.Inject

internal class EpoxyMutableConfigsRowProvider @Inject constructor(
  private val setupsSet: Set<@JvmSuppressWildcards MutableConfigSetup>,
  private val mutableConfig: MutableConfig
) : ConfigRowModelProvider {
  override fun epoxyModels(): List<Any> {
    return setupsSet.flatMap {
      it.mutableConfigs().map { configDef ->
        when (configDef.type) {
          MutableConfigType.BOOLEAN -> BooleanConfigModel(configDef, mutableConfig)
          MutableConfigType.LONG -> LongConfigModel(configDef, mutableConfig)
          MutableConfigType.STRING -> StringConfigModel(configDef, mutableConfig)
        }
      }
    }
  }
}
