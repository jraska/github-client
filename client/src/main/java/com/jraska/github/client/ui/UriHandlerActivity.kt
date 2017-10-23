package com.jraska.github.client.ui

import android.os.Bundle
import com.jraska.github.client.UriHandlerViewModel

class UriHandlerActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val viewModel = viewModel(UriHandlerViewModel::class.java)
    viewModel.handleDeepLink(intent.data.toString())
    finish()
  }
}
