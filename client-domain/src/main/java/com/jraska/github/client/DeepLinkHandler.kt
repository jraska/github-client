package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.common.toAnalyticsString
import com.jraska.github.client.logging.CrashReporter

import okhttp3.HttpUrl
import javax.inject.Inject

class DeepLinkHandler @Inject constructor(
  private val linkLauncher: DeepLinkLauncher,
  private val fallbackLauncher: WebLinkLauncher,
  private val crashReporter: CrashReporter,
  private val eventAnalytics: EventAnalytics
) {

  fun handleDeepLink(deepLink: HttpUrl) {
    var success = false

    try {
      linkLauncher.launch(deepLink)
      success = true
    } catch (ex: IllegalArgumentException) {
      crashReporter.report(ex, "Invalid deep link", deepLink.toString())
      fallbackLauncher.launch(deepLink)
    }

    val event = AnalyticsEvent.builder("deep_link_received")
      .addProperty("deep_link", deepLink.toAnalyticsString())
      .addProperty("success", success)
      .build()
    eventAnalytics.report(event)
  }
}
