package com.jraska.github.client.analytics

import com.jraska.github.client.Owner
import java.util.Collections

class AnalyticsEvent private constructor(
  val key: Key,
  val properties: Map<String, Any>
) {

  val name get() = key.name

  class Key(val name: String, val owner: Owner)

  class Builder internal constructor(private val key: Key) {
    private val properties = HashMap<String, Any>()

    private fun addAny(name: String, value: Any): Builder {
      properties[name] = value
      return this
    }

    fun addProperty(name: String, value: String): Builder {
      return addAny(name, value)
    }

    fun addProperty(name: String, value: Double): Builder {
      return addAny(name, value)
    }

    fun addProperty(name: String, value: Boolean): Builder {
      return addAny(name, value)
    }

    fun addProperty(name: String, value: Int): Builder {
      return addAny(name, value)
    }

    fun addProperty(name: String, value: Long): Builder {
      return addAny(name, value)
    }

    fun build(): AnalyticsEvent {
      return AnalyticsEvent(key, Collections.unmodifiableMap(properties))
    }
  }

  companion object {
    fun create(key: Key): AnalyticsEvent {
      return AnalyticsEvent(key, emptyMap())
    }

    fun builder(key: Key): Builder {
      return Builder(key)
    }
  }
}
