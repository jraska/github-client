package com.jraska.github.client.push

import com.jraska.github.client.DeepLinkHandler
import com.jraska.github.client.logging.CrashReporter
import okhttp3.HttpUrl
import javax.inject.Inject

internal class LaunchDeepLinkPushCommand @Inject constructor(
  private val deepLinkHandler: DeepLinkHandler,
  private val crashReporter: CrashReporter
) : PushActionCommand {
  override fun execute(action: PushAction): Boolean {
    val linkText = action.parameters["deepLink"] ?: return false
    val link: HttpUrl

    try {
      link = HttpUrl.get(linkText)
    } catch (ex: IllegalArgumentException) {
      crashReporter.report(ex, "Incorrect deep link provided $linkText")
      return false
    }

    deepLinkHandler.handleDeepLink(link)
    return true
  }
}
