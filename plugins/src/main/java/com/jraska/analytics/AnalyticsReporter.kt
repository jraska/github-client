package com.jraska.analytics

import com.mixpanel.mixpanelapi.MixpanelAPI

interface AnalyticsReporter {
  val name: String

  fun report(vararg events: AnalyticsEvent)

  companion object {
    fun create(reporterName: String): AnalyticsReporter {
      val mixpanelToken: String? = System.getenv("GITUB_CLIENT_MIXPANEL_API_KEY")
      if (mixpanelToken != null) {
        return MixpanelAnalyticsReporter(mixpanelToken, MixpanelAPI(), reporterName)
      } else {
        if (System.getenv("CI") == "true") {
          throw IllegalStateException("'GITHUB_CLIENT_MIXPANEL_API_KEY' not set on CI")
        } else {
          println("'GITHUB_CLIENT_MIXPANEL_API_KEY' not set, data will be reported to console only")
          return ConsoleReporter(reporterName)
        }
      }
    }
  }
}
