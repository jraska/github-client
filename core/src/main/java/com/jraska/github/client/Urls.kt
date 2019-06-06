package com.jraska.github.client

import okhttp3.HttpUrl

object Urls {
  fun user(login: String): HttpUrl {
    return HttpUrl.get("https://github.com/$login")
  }

  fun repo(fullPath: String): HttpUrl {
    return HttpUrl.get("https://github.com/$fullPath")
  }

  fun settings(): HttpUrl {
    return HttpUrl.get("https://github.com/settings")
  }

  fun about(): HttpUrl {
    return HttpUrl.get("https://github.com/about")
  }
}
