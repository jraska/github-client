package com.jraska.github.client.inappupdate

import android.app.Application
import android.content.Context
import com.jraska.github.client.core.android.OnAppCreate
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
object InAppUpdateModule {
  @Provides
  @IntoSet
  internal fun checkOnAppCreate(checkScheduler: UpdateCheckScheduler): OnAppCreate {
    return object : OnAppCreate {
      override fun onCreate(app: Application) {
        checkScheduler.startNonBlockingCheck()
      }
    }
  }

  @Provides
  internal fun appManagerFactory(context: Context): UpdateManagerFactory {
    return AppUpdateManagerFactoryProxy(context)
  }
}
