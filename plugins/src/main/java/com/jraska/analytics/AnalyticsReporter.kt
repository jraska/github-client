package com.jraska.analytics

import com.mixpanel.mixpanelapi.MixpanelAPI

interface AnalyticsReporter {
  fun report(vararg events: AnalyticsEvent)

  companion object {
    fun create(reporterName: String): AnalyticsReporter {
      val mixpanelToken: String? = System.getenv("GITHUB_CLIENT_MIXPANEL_API_KEY")
      if (mixpanelToken == null) {
        println("'GITHUB_CLIENT_MIXPANEL_API_KEY' not set, data will be reported to console only")
        return ConsoleReporter(reporterName)
      } else {
        return MixpanelAnalyticsReporter(mixpanelToken, MixpanelAPI(), reporterName)
      }
    }
  }
}
