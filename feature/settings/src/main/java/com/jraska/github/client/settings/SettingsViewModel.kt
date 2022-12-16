package com.jraska.github.client.settings

import androidx.lifecycle.ViewModel
import com.jraska.github.client.DeepLinkLauncher
import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.config.debug.ui.ConfigRowModelProvider
import com.jraska.github.client.navigation.Urls
import javax.inject.Inject

internal class SettingsViewModel @Inject constructor(
  private val eventAnalytics: EventAnalytics,
  private val deepLinkLauncher: DeepLinkLauncher,
  private val rowModelProvider: ConfigRowModelProvider,
  private val integrityCheck: IntegrityCheck
) : ViewModel() {
  fun onPurchaseSubmitted(value: String) {
    val money = value.toDoubleOrNull() ?: return

    val event = AnalyticsEvent.builder(ANALYTICS_ECOMMERCE_PURCHASE)
      .addProperty("value", money)
      .addProperty("currency", "USD")
      .build()

    eventAnalytics.report(event)
  }

  fun configRows() = rowModelProvider.epoxyModels()

  fun onConsoleClick() {
    deepLinkLauncher.launch(Urls.console())
  }

  fun onIntegrityCheckClicked() {
    integrityCheck.run()
  }

  companion object {
    val ANALYTICS_ECOMMERCE_PURCHASE = AnalyticsEvent.Key("purchase", Owner.USERS_TEAM)
  }
}
