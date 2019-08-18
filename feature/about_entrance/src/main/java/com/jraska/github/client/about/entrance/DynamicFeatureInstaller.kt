package com.jraska.github.client.about.entrance

import io.reactivex.Completable

interface DynamicFeatureInstaller {
  fun ensureInstalled(featureName: String): Completable
}
