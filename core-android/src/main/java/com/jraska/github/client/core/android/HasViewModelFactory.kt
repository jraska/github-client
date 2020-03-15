package com.jraska.github.client.core.android

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

interface HasViewModelFactory {
  fun factory(): ViewModelProvider.Factory
}

fun <T : ViewModel> FragmentActivity.viewModel(modelClass: Class<T>): T {
  val factory = (application as HasViewModelFactory).factory()
  return ViewModelProvider(this, factory).get(modelClass)
}
