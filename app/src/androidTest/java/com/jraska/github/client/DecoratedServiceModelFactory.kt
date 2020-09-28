package com.jraska.github.client

import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.push.PushHandleModel
import com.jraska.github.client.xpush.PushAwaitRule

class DecoratedServiceModelFactory(val productionFactory: ServiceModel.Factory) : ServiceModel.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ServiceModel> create(modelClass: Class<T>): T {
    if (modelClass == PushHandleModel::class.java) {
      return PushAwaitRule.TestPushHandleModel(productionFactory.create(modelClass)) as T
    }

    return productionFactory.create(modelClass)
  }
}
