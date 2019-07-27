package com.jraska.github.client

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

object Urls {
  fun user(login: String): HttpUrl {
    return "https://github.com/$login".toHttpUrl()
  }

  fun repo(fullPath: String): HttpUrl {
    return "https://github.com/$fullPath".toHttpUrl()
  }

  fun settings(): HttpUrl {
    return "https://github.com/settings".toHttpUrl()
  }

  fun about(): HttpUrl {
    return "https://github.com/about".toHttpUrl()
  }
}
