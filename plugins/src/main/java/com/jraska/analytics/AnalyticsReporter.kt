package com.jraska.analytics

import com.mixpanel.mixpanelapi.MixpanelAPI

interface AnalyticsReporter {
  fun report(vararg events: AnalyticsEvent)

  companion object {
    fun create(reporterName: String): AnalyticsReporter {
      val mixpanelToken: String? = System.getenv("GITHUB_CLIENT_MIXPANEL_API_KEY")
      if (mixpanelToken == null) {
        return ConsoleReporter(reporterName)
      } else {
        println("The key length is ${mixpanelToken.length}")
        return MixpanelAnalyticsReporter(mixpanelToken, MixpanelAPI(), reporterName)
      }
    }
  }
}
