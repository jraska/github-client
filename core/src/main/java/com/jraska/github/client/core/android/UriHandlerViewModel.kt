package com.jraska.github.client.core.android

import androidx.lifecycle.ViewModel
import com.jraska.github.client.DeepLinkHandler
import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.analytics.toAnalyticsString
import com.jraska.github.client.common.toBoolean
import okhttp3.HttpUrl
import javax.inject.Inject

class UriHandlerViewModel @Inject constructor(
  private val deepLinkHandler: DeepLinkHandler,
  private val eventAnalytics: EventAnalytics
) : ViewModel() {
  fun handleDeepLink(deepLink: HttpUrl) {
    val success = deepLinkHandler.handleDeepLink(deepLink)

    val event = AnalyticsEvent.builder(ANALYTICS_DEEP_LINK_KEY)
      .addProperty("deep_link", deepLink.toAnalyticsString())
      .addProperty("success", success.toBoolean())
      .build()
    eventAnalytics.report(event)
  }

  companion object {
    val ANALYTICS_DEEP_LINK_KEY = AnalyticsEvent.Key("deep_link_received", Owner.CORE_TEAM)
  }
}
