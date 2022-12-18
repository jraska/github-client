package com.jraska.github.client.push

import com.jraska.github.client.DeepLinkHandler
import com.jraska.github.client.logging.CrashReporter
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

internal class LaunchDeepLinkCommand @Inject constructor(
  private val deepLinkHandler: DeepLinkHandler,
  private val crashReporter: CrashReporter
) : PushActionCommand {
  override fun execute(action: PushAction): PushExecuteResult {
    val linkText = action.parameters["deepLink"] ?: return PushExecuteResult.FAILURE
    val link: HttpUrl

    try {
      link = linkText.toHttpUrl()
    } catch (ex: IllegalArgumentException) {
      crashReporter.report(ex, "Incorrect deep link provided $linkText")
      return PushExecuteResult.FAILURE
    }

    deepLinkHandler.handleDeepLink(link)
    return PushExecuteResult.SUCCESS
  }
}
