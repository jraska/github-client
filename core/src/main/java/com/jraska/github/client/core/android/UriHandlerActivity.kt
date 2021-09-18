package com.jraska.github.client.core.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class UriHandlerActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val viewModel = viewModel(UriHandlerViewModel::class.java)
    viewModel.handleDeepLink(inputUrl())
    finish()
  }
}
