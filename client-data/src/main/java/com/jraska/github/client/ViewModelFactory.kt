package com.jraska.github.client

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject internal constructor(
  private val providersMap: Map<Class<*>, @JvmSuppressWildcards Provider<ViewModel>>
)
  : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(aClass: Class<T>): T {
    val provider = providersMap[aClass] ?: throw IllegalArgumentException("There is no provider registered for $aClass")

    return provider.get() as T
  }
}
