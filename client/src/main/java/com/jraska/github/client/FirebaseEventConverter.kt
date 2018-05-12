package com.jraska.github.client

import android.os.Bundle

object FirebaseEventConverter {
  fun firebaseBundle(properties: Map<String, Any>): Bundle? {
    if (properties.isEmpty()) {
      return null
    }

    val parameters = Bundle()
    for ((key, value) in properties) {
      if (value is String) {
        parameters.putString(key, value)
      } else if (value is Double) {
        parameters.putDouble(key, value)
      } else if (value is Boolean) {
        val numericToSaveQuotas: Int
        if (value) {
          numericToSaveQuotas = 1
        } else {
          numericToSaveQuotas = 0
        }
        parameters.putInt(key, numericToSaveQuotas)
      } else if (value is Long) {
        parameters.putLong(key, value)
      } else if (value is Int) {
        parameters.putInt(key, value)
      } else if (value is Float) {
        parameters.putFloat(key, value)
      } else {
        parameters.putString(key, value.toString())
      }
    }

    return parameters
  }
}
