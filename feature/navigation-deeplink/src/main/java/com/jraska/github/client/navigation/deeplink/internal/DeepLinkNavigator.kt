package com.jraska.github.client.navigation.deeplink.internal

import com.jraska.github.client.DeepLinkLauncher
import com.jraska.github.client.WebLinkLauncher
import com.jraska.github.client.navigation.Navigator
import com.jraska.github.client.navigation.Urls
import okhttp3.HttpUrl

/**
 * All navigation will be done through deep links,
 * even the internal one to avoid two different
 * ways how to handle activity start.
 */
class DeepLinkNavigator(
  private val deepLinkLauncher: DeepLinkLauncher
) : Navigator {

  private fun launch(url: HttpUrl) {
    deepLinkLauncher.launch(url)
  }

  override fun startUserDetail(login: String) {
    val url = Urls.user(login)
    launch(url)
  }

  override fun startRepoDetail(fullPath: String) {
    val url = Urls.repo(fullPath)
    launch(url)
  }

  override fun showSettings() {
    launch(Urls.settings())
  }

  override fun showAbout() {
    launch(Urls.about())
  }

  override fun startConsole() {
    launch(Urls.console())
  }
}
