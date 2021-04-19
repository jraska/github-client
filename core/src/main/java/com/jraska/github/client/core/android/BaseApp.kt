package com.jraska.github.client.core.android

import android.app.Application
import androidx.lifecycle.ViewModelProvider

abstract class BaseApp : Application(), HasViewModelFactory, HasServiceModelFactory {
  abstract val appComponent: AppBaseComponent

  override fun factory(): ViewModelProvider.Factory {
    return appComponent.viewModelFactory()
  }

  override fun serviceModelFactory(): ServiceModel.Factory {
    return appComponent.serviceModelFactory()
  }

  override fun onCreate() {
    super.onCreate()
    appComponent.onAppCreateActions().forEach { it.onCreate(this) }
  }
}
