package com.jraska.github.client.common

import java.util.*

class Maps private constructor() {
  companion object {

    fun <K, V> newHashMap(key: K, value: V): Map<K, V> {
      val map = HashMap<K, V>()
      map.put(key, value)
      return map
    }
  }
}
