package com.jraska.github.client.about.entrance.internal

import com.jraska.github.client.dynamicbase.DynamicFeature
import javax.inject.Provider

internal class ReflectiveSettingsFeatureProvider : Provider<DynamicFeature> {
  override fun get(): DynamicFeature {
    return Class.forName("com.jraska.github.client.settings.SettingsFeature").newInstance() as DynamicFeature
  }
}
