package com.jraska.github.client

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.jraska.github.client.core.android.TopActivityProvider
import okhttp3.HttpUrl

internal class ChromeCustomTabsLauncher(
  private val provider: TopActivityProvider
) : WebLinkLauncher {
  override fun launch(url: HttpUrl) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    val uri = Uri.parse(url.toString())

    provider.onTopActivity { customTabsIntent.launchUrl(it, uri) }
  }
}
