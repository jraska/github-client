package com.jraska.github.client.chrome

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.jraska.github.client.WebLinkLauncher
import com.jraska.github.client.core.android.TopActivityProvider
import okhttp3.HttpUrl
import javax.inject.Inject

internal class ChromeCustomTabsLauncher @Inject constructor(
  private val provider: TopActivityProvider,
) : WebLinkLauncher {
  override fun launchOnWeb(url: HttpUrl) {
    val uri = Uri.parse(url.toString())
    val customTabsIntent = CustomTabsIntent.Builder().build()

    provider.onTopActivity { customTabsIntent.launchUrl(it, uri) }
  }
}
