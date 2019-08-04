package com.jraska.github.client.shortcuts

import androidx.lifecycle.ViewModel
import com.jraska.github.client.DeepLinkHandler

import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics

import okhttp3.HttpUrl
import javax.inject.Inject

internal class ShortcutHandlerModel @Inject constructor(
  private val deepLinkHandler: DeepLinkHandler,
  private val eventAnalytics: EventAnalytics
) : ViewModel() {

  internal fun handleDeepLink(url: HttpUrl) {
    val event = AnalyticsEvent.builder("shortcut_clicked")
      .addProperty("shortcut", url.toString())
      .build()

    eventAnalytics.report(event)

    deepLinkHandler.handleDeepLink(url)
  }
}
