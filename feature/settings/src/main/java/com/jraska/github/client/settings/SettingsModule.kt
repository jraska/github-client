package com.jraska.github.client.settings

import androidx.lifecycle.ViewModel
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.core.android.OnAppCreate
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet

@Module
abstract class SettingsModule {

  @Binds
  @IntoMap
  @ClassKey(SettingsViewModel::class)
  internal abstract fun provideUserDetailModel(viewModel: SettingsViewModel): ViewModel

  @Binds
  @IntoSet
  internal abstract fun provideSettingsLauncher(launcher: SettingsLinkLauncher): LinkLauncher

  @Binds
  @IntoSet
  internal abstract fun provideConsoleLogging(logginngSetup: SetupConsoleLogging) : OnAppCreate
}
