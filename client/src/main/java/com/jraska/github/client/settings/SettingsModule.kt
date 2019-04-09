package com.jraska.github.client.settings

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
object SettingsModule {
  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(SettingsViewModel::class)
  fun provideUserDetailModel(viewModel: SettingsViewModel): ViewModel {
    return viewModel
  }
}
