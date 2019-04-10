package com.jraska.github.client.core.android

import androidx.lifecycle.ViewModel
import com.jraska.github.client.DeepLinkHandler
import okhttp3.HttpUrl

class UriHandlerViewModel constructor(private val deepLinkHandler: DeepLinkHandler) : ViewModel() {
  fun handleDeepLink(deepLink: HttpUrl) {
    deepLinkHandler.handleDeepLink(deepLink)
  }
}
