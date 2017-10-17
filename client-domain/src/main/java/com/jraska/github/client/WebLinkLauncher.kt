package com.jraska.github.client

import okhttp3.HttpUrl

interface WebLinkLauncher {
  fun launch(url: HttpUrl)
}
