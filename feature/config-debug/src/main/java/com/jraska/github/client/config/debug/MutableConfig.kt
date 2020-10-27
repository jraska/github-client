package com.jraska.github.client.config.debug

import com.jraska.github.client.Config

class MutableConfig(
  private val config: Config
) : Config {
  private val values: MutableMap<String, Any> = mutableMapOf()

  override fun getBoolean(key: Config.Key): Boolean {
    return (values[key.name] as Boolean? ?: config.getBoolean(key))
  }

  override fun getString(key: Config.Key): String {
    return (values[key.name] as String? ?: config.getString(key))
  }

  override fun getLong(key: Config.Key): Long {
    return (values[key.name] as Long? ?: config.getLong(key))
  }

  override fun triggerRefresh() = config.triggerRefresh()

  fun set(key: String, value: Any) {
    values[key] = value
  }

  fun resetToDefault(key: String) {
    values.remove(key)
  }
}
