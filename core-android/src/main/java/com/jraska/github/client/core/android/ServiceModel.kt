package com.jraska.github.client.core.android

/**
 * Interface to mark all model classes to be used within {@link Service}
 */
interface ServiceModel {

  interface Factory {
    fun <T : ServiceModel> create(modelClass: Class<T>): T
  }
}
