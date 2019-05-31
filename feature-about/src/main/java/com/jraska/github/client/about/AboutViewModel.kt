package com.jraska.github.client.about

import androidx.lifecycle.ViewModel
import com.jraska.github.client.Navigator
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.analytics.toAnalyticsString
import okhttp3.HttpUrl
import javax.inject.Inject

internal class AboutViewModel @Inject constructor(
  private val analytics: EventAnalytics,
  private val navigator: Navigator
) : ViewModel() {

  fun onGithubClick() {
    openUrl(HttpUrl.get("https://github.com/jraska"))
  }

  fun onProjectDescriptionClick() {
    openUrl(HttpUrl.get("https://github.com/jraska/github-client"))
  }

  fun onProfileClick() {
    openUrl(HttpUrl.get("http://jraska.com"))
  }

  private fun openUrl(url: HttpUrl) {
    val event = AnalyticsEvent.builder("about_clicked")
      .addProperty("url", url.toAnalyticsString())
      .build()

    analytics.report(event)

    navigator.launchOnWeb(url)
  }
}
