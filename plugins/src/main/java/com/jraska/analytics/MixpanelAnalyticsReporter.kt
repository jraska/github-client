package com.jraska.analytics

import com.mixpanel.mixpanelapi.ClientDelivery
import com.mixpanel.mixpanelapi.MessageBuilder
import com.mixpanel.mixpanelapi.MixpanelAPI
import org.json.JSONObject

class MixpanelAnalyticsReporter(
  private val apiKey: String,
  private val api: MixpanelAPI,
  private val distinctId: String
) : AnalyticsReporter {
  override val name = "Mixpanel"

  override fun report(vararg events: AnalyticsEvent) {
    val delivery = ClientDelivery()

    val messageBuilder = MessageBuilder(apiKey)

    events.forEach {
      val jsonEvent = messageBuilder.event(distinctId, it.name, JSONObject(it.properties))
      delivery.addMessage(jsonEvent)
    }

    api.deliver(delivery)
  }
}
