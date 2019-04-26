package com.jraska.github.client

import com.jraska.github.client.logging.CrashReporter
import okhttp3.HttpUrl
import javax.inject.Inject

class DeepLinkHandler @Inject constructor(
  private val linkLauncher: DeepLinkLauncher,
  private val fallbackLauncher: WebLinkLauncher,
  private val crashReporter: CrashReporter
) {

  fun handleDeepLink(deepLink: HttpUrl): Boolean {
    try {
      linkLauncher.launch(deepLink)
      return true
    } catch (ex: IllegalArgumentException) {
      crashReporter.report(ex, "Invalid deep link $deepLink")
      fallbackLauncher.launch(deepLink)
      return false
    }
  }
}
