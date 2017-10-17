package com.jraska.github.client

import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.logging.CrashReporter

import okhttp3.HttpUrl
import javax.inject.Inject

class DeepLinkHandler @Inject constructor(private val linkLauncher: DeepLinkLauncher,
                                          private val fallbackLauncher: WebLinkLauncher,
                                          private val crashReporter: CrashReporter,
                                          private val eventAnalytics: EventAnalytics) {

  fun handleDeepLink(deepLink: HttpUrl) {
    val event = AnalyticsEvent.builder("deep_link_received")
      .addProperty("deep_link", deepLink.toString())
      .build()
    eventAnalytics.report(event)

    try {
      linkLauncher.launch(deepLink)
    } catch (ex: IllegalArgumentException) {
      crashReporter.report(ex, "Invalid deep link", deepLink.toString())
      fallbackLauncher.launch(deepLink)
    }
  }
}
