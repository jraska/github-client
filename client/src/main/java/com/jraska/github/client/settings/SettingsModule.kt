package com.jraska.github.client.settings

import android.arch.lifecycle.ViewModel
import com.jraska.github.client.analytics.EventAnalytics
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
  fun provideUserDetailModel(analytics: EventAnalytics): ViewModel {
    return SettingsViewModel(analytics)
  }
}
