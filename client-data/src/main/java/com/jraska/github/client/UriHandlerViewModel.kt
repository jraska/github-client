package com.jraska.github.client

import androidx.lifecycle.ViewModel
import okhttp3.HttpUrl

class UriHandlerViewModel constructor(private val deepLinkHandler: DeepLinkHandler) : ViewModel() {
  fun handleDeepLink(deepLink: HttpUrl) {
    deepLinkHandler.handleDeepLink(deepLink)
  }
}
