package com.jraska.github.client

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

interface HasViewModelFactory {
  fun factory(): ViewModelProvider.Factory
}

fun <T : ViewModel> FragmentActivity.viewModel(modelClass: Class<T>): T {
  val factory = (application as HasViewModelFactory).factory()
  return ViewModelProviders.of(this, factory).get(modelClass)
}
