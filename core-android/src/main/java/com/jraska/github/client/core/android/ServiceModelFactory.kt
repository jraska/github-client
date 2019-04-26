package com.jraska.github.client.core.android

import javax.inject.Inject
import javax.inject.Provider

class ServiceModelFactory @Inject internal constructor(
  private val providersMap: Map<Class<*>, @JvmSuppressWildcards Provider<ServiceModel>>
) : ServiceModel.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ServiceModel> create(modelClass: Class<T>): T {
    val provider = providersMap[modelClass]
      ?: throw IllegalArgumentException("There is no provider registered for $modelClass")

    return provider.get() as T
  }
}
