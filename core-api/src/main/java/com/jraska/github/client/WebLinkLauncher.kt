package com.jraska.github.client

import okhttp3.HttpUrl

interface WebLinkLauncher {
  fun launchOnWeb(url: HttpUrl)
}
