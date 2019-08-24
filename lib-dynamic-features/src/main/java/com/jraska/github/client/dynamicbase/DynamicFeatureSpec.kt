package com.jraska.github.client.dynamicbase

import javax.inject.Provider

class DynamicFeatureSpec(
  val featureName: String,
  val featureProvider: Provider<DynamicFeature>
)
