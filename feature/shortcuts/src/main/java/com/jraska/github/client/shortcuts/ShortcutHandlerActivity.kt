package com.jraska.github.client.shortcuts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jraska.github.client.core.android.inputUrl
import com.jraska.github.client.core.android.viewModel

internal class ShortcutHandlerActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val handlerModel = viewModel(ShortcutHandlerModel::class.java)

    val httpUrl = inputUrl()
    handlerModel.handleDeepLink(httpUrl)

    finish()
  }
}
