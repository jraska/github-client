package com.jraska.github.client.about

import androidx.annotation.Keep
import com.jraska.github.client.dynamicbase.DynamicFeature
import timber.log.Timber

@Keep // Used by reflection
class AboutFeature : DynamicFeature {
  override fun onLoad() {
    Timber.i("about feature loaded")
  }
}
