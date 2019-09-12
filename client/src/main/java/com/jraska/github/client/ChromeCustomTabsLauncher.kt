package com.jraska.github.client

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.jraska.github.client.core.android.TopActivityProvider
import com.jraska.github.client.rx.AppSchedulers
import okhttp3.HttpUrl
import timber.log.Timber

internal class ChromeCustomTabsLauncher(
  private val provider: TopActivityProvider,
  private val appSchedulers: AppSchedulers
) : WebLinkLauncher {
  override fun launch(url: HttpUrl) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    val uri = Uri.parse(url.toString())

    provider.resumedActivity()
      .observeOn(appSchedulers.mainThread)
      .subscribe({ customTabsIntent.launchUrl(it, uri) }, { Timber.e(it) })
  }
}
