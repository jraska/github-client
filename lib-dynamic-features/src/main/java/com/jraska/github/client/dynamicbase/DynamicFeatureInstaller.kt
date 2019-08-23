package com.jraska.github.client.dynamicbase

import io.reactivex.Completable

interface DynamicFeatureInstaller {
  fun ensureInstalled(featureName: String): Completable
}
