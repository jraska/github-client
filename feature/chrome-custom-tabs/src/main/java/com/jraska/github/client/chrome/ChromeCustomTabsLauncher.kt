package com.jraska.github.client.chrome

import android.content.pm.PackageManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.jraska.github.client.WebLinkLauncher
import com.jraska.github.client.core.android.TopActivityProvider
import okhttp3.HttpUrl

private const val CHROME_BROWSER_PACKAGE = "com.android.chrome"

internal class ChromeCustomTabsLauncher(
  private val provider: TopActivityProvider,
  private val packageManager: PackageManager
) : WebLinkLauncher {
  override fun launchOnWeb(url: HttpUrl) {
    val uri = Uri.parse(url.toString())

    val customTabsIntent = prepareIntent(uri)
    provider.onTopActivity { customTabsIntent.launchUrl(it, uri) }
  }

  private fun prepareIntent(uri: Uri): CustomTabsIntent {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.intent.data = uri

    val browsersToHandler = packageManager.queryIntentActivities(customTabsIntent.intent, 0)
    return when (browsersToHandler.size) {
      0 -> throw IllegalStateException("No app to launch deep link $uri")
      1 -> customTabsIntent
      else -> {
        val chromeAvailable = null != browsersToHandler.find { it.activityInfo?.packageName == CHROME_BROWSER_PACKAGE }
        if (chromeAvailable) {
          customTabsIntent.intent.`package` = CHROME_BROWSER_PACKAGE
        }
        customTabsIntent
      }
    }
  }
}
