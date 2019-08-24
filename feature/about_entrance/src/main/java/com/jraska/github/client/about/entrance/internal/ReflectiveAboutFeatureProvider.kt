package com.jraska.github.client.about.entrance.internal

import com.jraska.github.client.dynamicbase.DynamicFeature
import javax.inject.Provider

class ReflectiveAboutFeatureProvider : Provider<DynamicFeature> {
  override fun get(): DynamicFeature {
    return Class.forName("com.jraska.github.client.about.AboutFeature").newInstance() as DynamicFeature
  }
}
