package com.jraska.github.client

import com.jraska.github.client.common.BooleanResult
import com.jraska.github.client.logging.CrashReporter
import okhttp3.HttpUrl
import javax.inject.Inject

class DeepLinkHandlerImpl @Inject constructor(
  private val linkLauncher: DeepLinkLauncher,
  private val fallbackLauncher: WebLinkLauncher,
  private val crashReporter: CrashReporter
) : DeepLinkHandler {

  override fun handleDeepLink(deepLink: HttpUrl): BooleanResult {
    try {
      linkLauncher.launch(deepLink)
      return BooleanResult.SUCCESS
    } catch (ex: IllegalArgumentException) {
      crashReporter.report(ex, "Invalid deep link $deepLink")
      fallbackLauncher.launch(deepLink)
      return BooleanResult.FAILURE
    }
  }
}
