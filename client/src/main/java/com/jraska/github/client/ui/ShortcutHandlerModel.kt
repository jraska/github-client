package com.jraska.github.client.ui

import androidx.lifecycle.ViewModel

import com.jraska.github.client.DeepLinkLauncher
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics

import okhttp3.HttpUrl
import javax.inject.Inject

class ShortcutHandlerModel @Inject constructor(
  private val deepLinkLauncher: DeepLinkLauncher,
  private val eventAnalytics: EventAnalytics
) : ViewModel() {

  internal fun handleDeepLink(url: HttpUrl) {
    val event = AnalyticsEvent.builder("shortcut_clicked")
      .addProperty("shortcut", url.toString())
      .build()

    eventAnalytics.report(event)

    deepLinkLauncher.launch(url)
  }
}
