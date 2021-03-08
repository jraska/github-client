package com.jraska.github.client.inappupdate

import android.app.Application
import android.content.Context
import com.jraska.github.client.config.MutableConfigDef
import com.jraska.github.client.config.MutableConfigSetup
import com.jraska.github.client.config.MutableConfigType
import com.jraska.github.client.core.android.OnAppCreate
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
object InAppUpdateModule {
  @Provides
  internal fun appManagerFactory(context: Context): UpdateManagerFactory {
    return AppUpdateManagerFactoryProxy(context)
  }

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
  @IntoSet
  internal fun debugConfigs(): MutableConfigSetup {
    return object : MutableConfigSetup {
      override fun mutableConfigs(): List<MutableConfigDef> {
        return listOf(
          MutableConfigDef(
            UpdateChecker.KEY_UPDATE_STRATEGY,
            MutableConfigType.STRING,
            listOf("Flexible", "Immediate", "", "Off", "Error value ^%@*&")
          )
        )
      }
    }
  }
}
