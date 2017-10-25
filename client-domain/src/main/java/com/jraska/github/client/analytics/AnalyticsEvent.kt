package com.jraska.github.client.analytics

import java.util.*

class AnalyticsEvent private constructor(val name: String, val properties: Map<String, String>) {

  class Builder internal constructor(private val name: String) {
    private val properties = HashMap<String, String>()

    fun addProperty(name: String, value: String): Builder {
      properties.put(name, value)
      return this
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
