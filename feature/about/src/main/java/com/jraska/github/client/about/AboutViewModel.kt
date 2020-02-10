package com.jraska.github.client.about

import androidx.lifecycle.ViewModel
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.analytics.toAnalyticsString
import com.jraska.github.client.identity.IdentityProvider
import com.jraska.github.client.navigation.Navigator
import javax.inject.Inject
import okhttp3.HttpUrl.Companion.toHttpUrl

internal class AboutViewModel @Inject constructor(
  private val analytics: EventAnalytics,
  private val navigator: Navigator,
  private val identityProvider: IdentityProvider
) : ViewModel() {

  fun onProjectDescriptionClick() {
    openUrl("https://github.com/jraska/github-client")
  }

  fun onGithubClick() {
    openUrl("https://github.com/jraska")
  }

  fun onWebClick() {
    openUrl("http://jraska.com")
  }

  fun onMediumClick() {
    openUrl("https://medium.com/@josef.raska")
  }

  fun onTwitterClick() {
    openUrl("https://twitter.com/josef_raska")
  }

  private fun openUrl(urlText: String) {
    val url = urlText.toHttpUrl()
    val event = AnalyticsEvent.builder("about_clicked")
      .addProperty("url", url.toAnalyticsString())
      .addProperty("user_id", identityProvider.session().userId.toString())
      .build()

    analytics.report(event)

    navigator.launchOnWeb(url)
  }
}
