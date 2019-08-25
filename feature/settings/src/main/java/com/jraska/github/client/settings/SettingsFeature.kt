package com.jraska.github.client.settings

import androidx.annotation.Keep
import com.jraska.github.client.dynamicbase.DynamicFeature
import timber.log.Timber

@Keep // used by reflection
class SettingsFeature : DynamicFeature {
  override fun onFeatureCreate() {
    SetupConsoleLogging().onCreate()
    Timber.i("Settings feature loaded")
  }
}
