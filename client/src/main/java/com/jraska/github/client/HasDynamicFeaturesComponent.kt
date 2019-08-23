package com.jraska.github.client

import androidx.fragment.app.FragmentActivity

interface HasDynamicFeaturesComponent {
  fun dynamicFeaturesComponent(): DynamicFeaturesComponent
}

fun FragmentActivity.dynamicFeaturesComponent() = (application as HasDynamicFeaturesComponent).dynamicFeaturesComponent()
