package com.jraska.github.client

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.test.InstrumentationRegistry
import javax.inject.Provider

class ViewModelFactoryDecorator(
  private val decoratedFactory: ViewModelProvider.Factory,
  private val providersMap: Map<Class<*>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(aClass: Class<T>): T {
    val viewModelProvider = providersMap[aClass]
    return if (viewModelProvider != null) {
      viewModelProvider.get() as T
    } else {
      decoratedFactory.create(aClass)
    }
  }

  companion object {
    fun setToApp(
      viewModelClass: Class<out ViewModel>,
      implementation: ViewModel
    ) {
      val applicationContext = InstrumentationRegistry.getTargetContext().applicationContext
      setToApp(applicationContext as GitHubClientApp, viewModelClass, implementation)
    }

    private fun setToApp(app: GitHubClientApp, viewModelClass: Class<out ViewModel>, implementation: ViewModel) {
      val map = HashMap<Class<*>, Provider<ViewModel>>()
      map[viewModelClass] = Provider { implementation }

      app.viewModelFactory = ViewModelFactoryDecorator(app.viewModelFactory, map)
    }

    fun removeDecorations() {
      val app = InstrumentationRegistry.getTargetContext().applicationContext as GitHubClientApp

      while (app.viewModelFactory is ViewModelFactoryDecorator) {
        val decorator = app.viewModelFactory as ViewModelFactoryDecorator
        app.viewModelFactory = decorator.decoratedFactory
      }
    }
  }
}
