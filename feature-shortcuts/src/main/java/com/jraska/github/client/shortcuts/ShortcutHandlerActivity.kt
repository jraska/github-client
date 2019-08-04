package com.jraska.github.client.shortcuts

import android.os.Bundle
import com.jraska.github.client.core.android.BaseActivity
import com.jraska.github.client.core.android.inputUrl
import com.jraska.github.client.core.android.viewModel

internal class ShortcutHandlerActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val handlerModel = viewModel(ShortcutHandlerModel::class.java)

    val httpUrl = inputUrl()
    handlerModel.handleDeepLink(httpUrl)

    finish()
  }
}
