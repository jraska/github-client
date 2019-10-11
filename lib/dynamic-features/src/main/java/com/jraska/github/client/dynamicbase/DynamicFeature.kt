package com.jraska.github.client.dynamicbase

interface DynamicFeature {

  /**
   * This method is called right after installation of the feature
   * or on `Application.onCreate` in case the feature is already installed.
   */
  fun onFeatureCreate()
}
