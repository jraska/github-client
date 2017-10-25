package com.jraska.github.client.push

import java.util.*

class PushAction private constructor(val name: String, val parameters: Map<String, String>) {
  companion object {
    val KEY_ACTION = "action"

    private val DEFAULT_ACTION = "message_without_data"
    val DEFAULT = create(DEFAULT_ACTION)

    @JvmOverloads
    fun create(name: String, properties: Map<String, String> = emptyMap()): PushAction {
      return PushAction(name, Collections.unmodifiableMap(properties))
    }
  }
}
