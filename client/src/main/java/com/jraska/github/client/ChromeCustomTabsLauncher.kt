package com.jraska.github.client

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.jraska.github.client.ui.BaseActivity
import okhttp3.HttpUrl

import javax.inject.Provider

internal class ChromeCustomTabsLauncher(private val provider: Provider<BaseActivity>) : WebLinkLauncher {
  override fun launch(url: HttpUrl) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    val uri = Uri.parse(url.toString())

    customTabsIntent.launchUrl(provider.get(), uri)
  }
}
