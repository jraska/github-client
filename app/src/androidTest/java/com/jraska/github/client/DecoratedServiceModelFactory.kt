package com.jraska.github.client

import com.jraska.github.client.core.android.ServiceModel

class DecoratedServiceModelFactory(val productionFactory: ServiceModel.Factory) : ServiceModel.Factory {
  var decorator: Decorator? = null

  override fun <T : ServiceModel> create(modelClass: Class<T>): T {
    return decorator?.create(modelClass, productionFactory) ?: productionFactory.create(modelClass)
  }

  interface Decorator {
    fun <T : ServiceModel> create(modelClass: Class<T>, productionFactory: ServiceModel.Factory): T
  }
}
