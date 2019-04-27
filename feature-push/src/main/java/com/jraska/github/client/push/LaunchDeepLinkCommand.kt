package com.jraska.github.client.push

import com.jraska.github.client.DeepLinkHandler
import com.jraska.github.client.common.BooleanResult
import com.jraska.github.client.common.BooleanResult.FAILURE
import com.jraska.github.client.common.BooleanResult.SUCCESS
import com.jraska.github.client.logging.CrashReporter
import okhttp3.HttpUrl
import javax.inject.Inject

internal class LaunchDeepLinkCommand @Inject constructor(
  private val deepLinkHandler: DeepLinkHandler,
  private val crashReporter: CrashReporter
) : PushActionCommand {
  override fun execute(action: PushAction): BooleanResult {
    val linkText = action.parameters["deepLink"] ?: return FAILURE
    val link: HttpUrl

    try {
      link = HttpUrl.get(linkText)
    } catch (ex: IllegalArgumentException) {
      crashReporter.report(ex, "Incorrect deep link provided $linkText")
      return FAILURE
    }

    deepLinkHandler.handleDeepLink(link)
    return SUCCESS
  }
}
