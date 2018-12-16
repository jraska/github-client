package com.jraska.github.client

import androidx.lifecycle.ViewModel
import okhttp3.HttpUrl

class UriHandlerViewModel constructor(private val deepLinkHandler: DeepLinkHandler) : ViewModel() {
  fun handleDeepLink(deepLinkText: String) {
    val deepLink = HttpUrl.get(deepLinkText)
    deepLinkHandler.handleDeepLink(deepLink)
  }
}
