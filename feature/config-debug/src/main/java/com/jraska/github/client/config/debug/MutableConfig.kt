package com.jraska.github.client.config.debug

import com.jraska.github.client.Config

class MutableConfig(
  private val config: Config
) : Config {
  private val values: MutableMap<String, Any> = mutableMapOf()

  override fun getBoolean(key: Config.Key): Boolean {
    return (values[key.name] as Boolean? ?: config.getBoolean(key))
  }

  fun setBoolean(key: Config.Key, value: Boolean) {
    values[key.name] = value
  }

  override fun getString(key: Config.Key): String {
    return (values[key.name] as String? ?: config.getString(key))
  }

  fun setString(key: Config.Key, value: String) {
    values[key.name] = value
  }

  override fun getLong(key: Config.Key): Long {
    return (values[key.name] as Long? ?: config.getLong(key))
  }

  fun setLong(key: Config.Key, value: Long) {
    values[key.name] = value
  }

  override fun triggerRefresh() = config.triggerRefresh()

  fun resetToDefault(key: Config.Key) {
    values.remove(key.name)
  }
}
