package com.jraska.github.client.users

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.jraska.github.client.core.android.HasViewModelFactory

class UsersApp : Application(), HasViewModelFactory {
  private val appComponent: UsersAppComponent by lazy { DaggerUsersAppComponent.factory().create(this) }

  override fun factory(): ViewModelProvider.Factory = appComponent.viewModelFactory()

  override fun onCreate() {
    super.onCreate()

    appComponent.onAppCreateActions().forEach {
      it.onCreate(this)
    }
  }
}
