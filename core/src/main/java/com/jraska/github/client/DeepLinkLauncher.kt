package com.jraska.github.client

import okhttp3.HttpUrl

interface DeepLinkLauncher {
    fun launch(deepLink: HttpUrl)
}
