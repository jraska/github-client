package com.jraska.github.client

import android.app.Activity
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import okhttp3.HttpUrl
import javax.inject.Provider

internal class ChromeCustomTabsLauncher(private val provider: Provider<Activity>) : WebLinkLauncher {
  override fun launch(url: HttpUrl) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    val uri = Uri.parse(url.toString())

    customTabsIntent.launchUrl(provider.get(), uri)
  }
}
