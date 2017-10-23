package com.jraska.github.client.ui

import android.os.Bundle

import okhttp3.HttpUrl

class ShortcutHandlerActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val handlerModel = viewModel(ShortcutHandlerModel::class.java)

    val httpUrl = HttpUrl.parse(intent.data.toString())!!
    handlerModel.handleDeepLink(httpUrl)

    finish()
  }
}
