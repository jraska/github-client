package com.jraska.github.client.inappupdate

import android.app.Application
import com.jraska.github.client.core.android.OnAppCreate
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
object InAppUpdateModule {
  @Provides
  @IntoSet
  internal fun checkOnAppCreate(updateChecker: UpdateChecker): OnAppCreate {
    return object : OnAppCreate {
      override fun onCreate(app: Application) {
        updateChecker.checkForUpdates()
      }
    }
  }
}
