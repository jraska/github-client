package com.jraska.github.client

import android.arch.lifecycle.ViewModel
import okhttp3.HttpUrl

class UriHandlerViewModel internal constructor(private val deepLinkHandler: DeepLinkHandler) : ViewModel() {
  fun handleDeepLink(deepLinkText: String) {
    val deepLink = HttpUrl.parse(deepLinkText)
    deepLinkHandler.handleDeepLink(deepLink!!)
  }
}
