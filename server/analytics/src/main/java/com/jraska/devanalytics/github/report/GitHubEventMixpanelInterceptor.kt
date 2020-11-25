package com.jraska.devanalytics.github.report

import com.jraska.devanalytics.github.model.Environment
import com.jraska.devanalytics.github.model.EventReader
import com.jraska.devanalytics.github.model.GitHubPrEvent
import java.io.BufferedReader

class GitHubEventMixpanelInterceptor(
  private val reporter: MixpanelReporter,
  private val eventReader: EventReader
) {
  fun intercept(reader: BufferedReader): GitHubPrEvent {
    val event = eventReader.parse(reader)
    reporter.report(event)

    return event
  }

  companion object {
    fun create(environment: Environment): GitHubEventMixpanelInterceptor {
      val reporter = MixpanelReporter.create(environment)
      val eventReader = EventReader.create()

      return GitHubEventMixpanelInterceptor(reporter, eventReader)
    }
  }
}
