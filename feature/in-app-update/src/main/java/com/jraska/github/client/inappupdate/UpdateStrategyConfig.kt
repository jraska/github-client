package com.jraska.github.client.inappupdate

import timber.log.Timber

internal enum class UpdateStrategyConfig {
  FLEXIBLE,
  IMMEDIATE,
  OFF,
  UNKNOWN;

  companion object {
    fun fromConfig(configValue: String): UpdateStrategyConfig {
      return when (configValue) {
        "Immediate" -> IMMEDIATE
        "Flexible" -> FLEXIBLE
        "", "Off" -> OFF
        else -> {
          Timber.e(
            "Update Strategy for the app was resolved as %s, this means misconfigured remote configuration value: '%s'",
            UNKNOWN, configValue
          )
          UNKNOWN
        }
      }
    }
  }
}
