package com.jraska.github.client.analytics

import java.util.Collections

class AnalyticsEvent private constructor(
  val name: String,
  val properties: Map<String, Any>
) {

  class Builder internal constructor(private val name: String) {
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
      return AnalyticsEvent(name, Collections.unmodifiableMap(properties))
    }
  }

  companion object {
    fun create(name: String): AnalyticsEvent {
      return AnalyticsEvent(name, emptyMap())
    }

    fun builder(name: String): Builder {
      return Builder(name)
    }
  }
}
