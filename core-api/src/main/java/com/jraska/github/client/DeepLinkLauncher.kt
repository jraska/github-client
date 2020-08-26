package com.jraska.github.client

import okhttp3.HttpUrl

interface DeepLinkLauncher {

  @Throws(IllegalArgumentException::class)
  fun launch(deepLink: HttpUrl)
}
