package com.jraska.github.client.about

import androidx.lifecycle.ViewModel
import com.jraska.github.client.core.android.LinkLauncher
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet

@Module
abstract class AboutBreakingMasterModule {

  @Binds
  @IntoMap
  @ClassKey(AboutViewModel::class)
  internal abstract fun provideViewModel(viewModel: AboutViewModel): ViewModel

  @Binds
  @IntoSet
  internal abstract fun provideLauncher(launcher: AboutLinkLauncher): LinkLauncher
}
