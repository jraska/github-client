package com.jraska.github.client

import okhttp3.HttpUrl

interface DeepLinkHandler {
  fun handleDeepLink(deepLink: HttpUrl): DeeplinkResult
}

enum class DeeplinkResult {
  SUCCESS,
  FAILURE
}
