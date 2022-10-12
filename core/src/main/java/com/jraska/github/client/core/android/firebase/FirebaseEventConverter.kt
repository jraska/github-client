package com.jraska.github.client.core.android.firebase

import android.os.Bundle

object FirebaseEventConverter {
  fun firebaseBundle(properties: Map<String, Any>): Bundle? {
    if (properties.isEmpty()) {
      return null
    }

    val parameters = Bundle()
    for ((key, value) in properties) {
      when (value) {
        is String -> parameters.putString(key, value)
        is Double -> parameters.putDouble(key, value)
        is Boolean -> parameters.putInt(key, numericToSaveQuotas(value))
        is Long -> parameters.putLong(key, value)
        is Int -> parameters.putInt(key, value)
        is Float -> parameters.putFloat(key, value)
        else -> parameters.putString(key, value.toString())
      }
    }

    return parameters
  }

  private fun numericToSaveQuotas(value: Boolean): Int {
    return if (value) 1 else 0
  }
}
