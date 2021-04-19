package com.jraska.github.client.core.android

import androidx.lifecycle.ViewModelProvider

interface AppBaseComponent {
  fun onAppCreateActions(): Set<OnAppCreate>

  fun serviceModelFactory(): ServiceModel.Factory

  fun viewModelFactory(): ViewModelProvider.Factory
}
