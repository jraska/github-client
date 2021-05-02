package com.jraska.github.client

import com.jraska.github.client.core.android.BaseApp
import com.jraska.github.client.core.android.HasServiceModelFactory
import com.jraska.github.client.core.android.HasViewModelFactory

open class GitHubClientApp : BaseApp(), HasViewModelFactory, HasServiceModelFactory {

  override val appComponent: AppComponent by lazy { createAppComponent() }

  open fun createAppComponent(): AppComponent {
    return DaggerAppComponent.factory().create(this)
  }
}
