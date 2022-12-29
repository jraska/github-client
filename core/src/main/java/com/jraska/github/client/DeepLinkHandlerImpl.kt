package com.jraska.github.client

import com.jraska.github.client.logging.CrashReporter
import okhttp3.HttpUrl
import javax.inject.Inject

class DeepLinkHandlerImpl @Inject constructor(
  private val linkLauncher: DeepLinkLauncher,
  private val fallbackLauncher: WebLinkLauncher,
  private val crashReporter: CrashReporter
) : DeepLinkHandler {

  override fun handleDeepLink(deepLink: HttpUrl): DeeplinkResult {
    try {
      linkLauncher.launch(deepLink)
      return DeeplinkResult.SUCCESS
    } catch (ex: IllegalArgumentException) {
      crashReporter.report(ex, "Invalid deep link $deepLink")
      fallbackLauncher.launchOnWeb(deepLink)
      return DeeplinkResult.FAILURE
    }
  }
}
