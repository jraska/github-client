package com.jraska.github.client

import okhttp3.HttpUrl

/**
 * All navigation will be done through deep links,
 * even the internal one to avoid two different
 * ways how to handle activity start.
 */
class DeepLinkNavigator(
  private val deepLinkLauncher: DeepLinkLauncher,
  private val webLinkLauncher: WebLinkLauncher
) : Navigator {

  private fun launch(url: HttpUrl) {
    deepLinkLauncher.launch(url)
  }

  override fun launchOnWeb(httpUrl: HttpUrl) {
    webLinkLauncher.launch(httpUrl)
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
}
