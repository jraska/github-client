package com.jraska.github.client.core.android

import androidx.lifecycle.ViewModel
import com.jraska.github.client.DeepLinkHandler
import okhttp3.HttpUrl
import javax.inject.Inject

class UriHandlerViewModel @Inject constructor(private val deepLinkHandler: DeepLinkHandler) : ViewModel() {
  fun handleDeepLink(deepLink: HttpUrl) {
    deepLinkHandler.handleDeepLink(deepLink)
  }
}
