package com.jraska.github.client.about

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
object AboutModule {

  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(AboutViewModel::class)
  internal fun provideViewModel(viewModel: AboutViewModel): ViewModel {
    return viewModel
  }
}
