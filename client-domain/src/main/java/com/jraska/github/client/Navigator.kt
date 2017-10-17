package com.jraska.github.client

import okhttp3.HttpUrl

interface Navigator {
  fun launchOnWeb(httpUrl: HttpUrl)

  fun startUserDetail(login: String)

  fun startRepoDetail(fullPath: String)
}
