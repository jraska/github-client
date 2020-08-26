package com.jraska.github.client.core.android

import android.os.Bundle

class UriHandlerActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val viewModel = viewModel(UriHandlerViewModel::class.java)
    viewModel.handleDeepLink(inputUrl())
    finish()
  }
}
