package com.jraska.github.client.push

class PushAction private constructor(val name: String, val parameters: Map<String, String>) {
  companion object {
    const val KEY_ACTION = "action"

    private const val DEFAULT_ACTION = "message_without_data"
    val DEFAULT = create(DEFAULT_ACTION)

    @JvmOverloads
    fun create(name: String, properties: Map<String, String> = emptyMap()): PushAction {
      return PushAction(name, properties)
    }
  }
}
