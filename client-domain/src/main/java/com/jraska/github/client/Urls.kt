package com.jraska.github.client

import okhttp3.HttpUrl

class Urls private constructor() {
  companion object {
    fun user(login: String): HttpUrl {
      return HttpUrl.parse("https://github.com/$login")!!
    }

    fun repo(fullPath: String): HttpUrl {
      return HttpUrl.parse("https://github.com/$fullPath")!!
    }

    fun settings(): HttpUrl {
      return HttpUrl.parse("https://github.com/settings")!!
    }
  }
}
