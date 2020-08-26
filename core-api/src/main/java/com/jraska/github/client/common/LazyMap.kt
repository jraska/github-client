package com.jraska.github.client.common

fun <K, V> lazyMap(initializer: (K) -> V): Map<K, V> {
  val map = mutableMapOf<K, V>()
  return map.withDefault { key ->
    val newValue = initializer(key)
    map[key] = newValue
    return@withDefault newValue
  }
}
