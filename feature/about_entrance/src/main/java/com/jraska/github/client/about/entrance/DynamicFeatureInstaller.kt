package com.jraska.github.client.about.entrance

import io.reactivex.Completable

interface DynamicFeatureInstaller {
  fun install(featureName: String): Completable
}
