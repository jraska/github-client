package com.jraska.github.client

import com.jraska.github.client.common.BooleanResult
import okhttp3.HttpUrl

interface DeepLinkHandler {
  fun handleDeepLink(deepLink: HttpUrl): BooleanResult
}
