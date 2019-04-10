package com.jraska.github.client.settings

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.jraska.github.client.core.android.LinkLauncher
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import okhttp3.HttpUrl

@Module
object SettingsModule {
  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(SettingsViewModel::class)
  fun provideUserDetailModel(viewModel: SettingsViewModel): ViewModel {
    return viewModel
  }

  @JvmStatic
  @Provides
  @IntoSet
  fun provideSettingsLauncher(): LinkLauncher {
    return object : LinkLauncher {
      override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
        return if ("/settings" == deepLink.encodedPath()) {
          SettingsActivity.start(inActivity)
          LinkLauncher.Result.LAUNCHED
        } else {
          LinkLauncher.Result.NOT_LAUNCHED
        }
      }

      override fun priority(): LinkLauncher.Priority {
        return LinkLauncher.Priority.EXACT_MATCH
      }
    }
  }
}
