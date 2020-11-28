package com.jraska.devanalytics.github.report

import com.jraska.devanalytics.github.model.Environment
import com.jraska.devanalytics.github.model.GitHubPrEvent
import com.mixpanel.mixpanelapi.ClientDelivery
import com.mixpanel.mixpanelapi.MessageBuilder
import com.mixpanel.mixpanelapi.MixpanelAPI
import org.json.JSONObject

class MixpanelReporter(
  private val api: MixpanelAPI,
  private val apiKey: String
) {
  fun report(event: GitHubPrEvent) {
    val delivery = ClientDelivery()

    val properties = convertEvent(event)
    val mixpanelEvent = MessageBuilder(apiKey)
      .event(SINGLE_NAME_FOR_ONE_USER, event.name, JSONObject(properties))

    delivery.addMessage(mixpanelEvent)

    api.deliver(delivery)
  }

  private fun convertEvent(event: GitHubPrEvent): Map<String, Any?> {
    return mapOf(
      "action" to event.action,
      "prUrl" to event.prUrl,
      "author" to event.author,
      "comment" to event.comment,
      "state" to event.state,
      "prNumber" to event.prNumber
    ).filter { it.value != null }
  }

  companion object {
    fun create(environment: Environment): MixpanelReporter {
      val mixpanelApi = if (environment.mixpanelUrl == null) {
        MixpanelAPI()
      } else {
        MixpanelAPI(environment.mixpanelUrl, environment.mixpanelUrl)
      }

      return MixpanelReporter(mixpanelApi, environment.mixpanelToken)
    }

    private val SINGLE_NAME_FOR_ONE_USER = "GitHub PRs Reporter"
  }
}
