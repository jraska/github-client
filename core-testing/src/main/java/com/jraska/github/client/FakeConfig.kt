package com.jraska.github.client

class FakeConfig private constructor(private val values: MutableMap<String, Any>) : Config {

  var configRefreshTriggered = false
    private set

  override fun triggerRefresh() {
    configRefreshTriggered = true
  }

  override fun getBoolean(key: Config.Key): Boolean {
    return (values[key.name] ?: false) as Boolean
  }

  override fun getString(key: Config.Key): String {
    return (values[key.name] ?: "") as String
  }

  override fun getLong(key: Config.Key): Long {
    return (values[key.name] ?: 0L) as Long
  }

  fun set(key: String, value: Any): RevertSet {
    val previousValue = values[key]
    val revertSet = RevertSet(this, key, previousValue)

    values[key] = value

    return revertSet
  }

  fun resetRefreshTracking() {
    configRefreshTriggered = false
  }

  class RevertSet(
    private val fakeConfig: FakeConfig,
    private val key: String,
    private val previousValue: Any?
  ) {
    fun revert() {
      if (previousValue == null) {
        fakeConfig.values.remove(key)
      } else {
        fakeConfig.values[key] = previousValue
      }
    }
  }

  companion object {
    fun create(values: Map<String, Any> = emptyMap()): FakeConfig {
      return FakeConfig(values.toMutableMap())
    }
  }
}
