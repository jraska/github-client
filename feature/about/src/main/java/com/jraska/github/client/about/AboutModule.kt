package com.jraska.github.client.about

import androidx.lifecycle.ViewModel
import com.jraska.github.client.core.android.LinkLauncher
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet

@Module
object AboutModule {

  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(AboutViewModel::class)
  internal fun provideViewModel(viewModel: AboutViewModel): ViewModel {
    return viewModel
  }

  @JvmStatic
  @Provides
  @IntoSet
  internal fun provideLauncher(): LinkLauncher {
    return AboutLinkLauncher()
  }
}
