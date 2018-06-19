package com.jraska.github.client

import java.util.*


class FakeConfig private constructor(private val values: Map<String, Any>) : Config {

  override fun triggerRefresh() {
    // do nothing
  }

  override fun getBoolean(key: String): Boolean {
    return values[key] as Boolean
  }

  override fun getString(key: String): String {
    return values[key] as String
  }

  override fun getLong(key: String): Long {
    return values[key] as Long
  }

  companion object {
    fun create(values: Map<String, Any> = emptyMap()): FakeConfig {
      return FakeConfig(Collections.unmodifiableMap(values))
    }
  }
}
