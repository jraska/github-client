package com.jraska.github.client.core.android

import android.app.Service

interface HasServiceModelFactory {
  fun serviceModelFactory(): ServiceModel.Factory
}

fun <T : ServiceModel> Service.serviceModel(modelClass: Class<T>): T {
  val factory = (application as HasServiceModelFactory).serviceModelFactory()
  return factory.create(modelClass)
}
