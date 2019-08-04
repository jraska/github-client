package com.jraska.github.client.shortcuts

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
object ShortcutsModule {
  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(com.jraska.github.client.shortcuts.ShortcutHandlerModel::class)
  internal fun uriShortcutViewModel(viewModel: ShortcutHandlerModel): ViewModel {
    return viewModel
  }
}
