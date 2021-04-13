package com.jraska.github.client.android

import com.jraska.github.client.DeepLinkLauncher
import okhttp3.HttpUrl

class RecordingDeepLinkLauncher: DeepLinkLauncher {
  val linksLaunched: MutableList<HttpUrl> = mutableListOf()

  override fun launch(deepLink: HttpUrl) {
    linksLaunched.add(deepLink)
  }
}
