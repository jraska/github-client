package com.jraska.github.client.ui

import android.os.Bundle
import com.jraska.github.client.core.android.UriHandlerViewModel
import com.jraska.github.client.inputUrl
import com.jraska.github.client.core.android.viewModel

class UriHandlerActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val viewModel = viewModel(UriHandlerViewModel::class.java)
    viewModel.handleDeepLink(inputUrl())
    finish()
  }
}
