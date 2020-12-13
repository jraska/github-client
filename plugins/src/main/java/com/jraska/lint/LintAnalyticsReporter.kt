package com.jraska.lint

import com.jraska.analytics.AnalyticsEvent
import com.jraska.analytics.AnalyticsReporter

class LintAnalyticsReporter(
  private val analyticsReporter: AnalyticsReporter
) {
  fun report(projectResult: LintProjectResult) {
    val allEvents = mutableListOf(convertProjectResult(projectResult))

    allEvents.addAll(
      projectResult.moduleResults
        .flatMap { convertModule(it) }
    )

    analyticsReporter.report(*allEvents.toTypedArray())

    println("$GRAPH_ICON Lint results reported $GRAPH_ICON")
  }

  private fun convertModule(moduleResult: LintModuleResult): List<AnalyticsEvent> {
    val moduleEvents = moduleResult.issues.map { convertIssue(it) }.toMutableList()

    moduleEvents.add(
      AnalyticsEvent(
        "Lint Module Result",
        mapOf(
          "totalIssues" to moduleResult.issuesCount,
          "totalWarnings" to moduleResult.warningsCount,
          "totalInfo" to moduleResult.infoCount,
          "totalErrors" to moduleResult.errorsCount,
          "totalIgnored" to moduleResult.ignoredCount,
          "moduleName" to moduleResult.metadata.moduleName,
          "moduleType" to moduleResult.metadata.type.name
        )
      )
    )

    return moduleEvents
  }

  private fun convertIssue(issue: LintIssue): AnalyticsEvent {
    return AnalyticsEvent(
      "Lint Issue Result",
      mapOf(
        "category" to issue.category,
        "errorLine" to issue.errorLine,
        "issueId" to issue.id,
        "location" to issue.location,
        "message" to issue.message,
        "moduleName" to issue.metadata.moduleName,
        "moduleType" to issue.metadata.type.name,
        "priority" to issue.priority,
        "severity" to issue.severity.name,
        "summary" to issue.summary,
      )
    )
  }

  private fun convertProjectResult(projectResult: LintProjectResult): AnalyticsEvent {
    return AnalyticsEvent(
      "Lint Project Result",
      mapOf(
        "totalIssues" to projectResult.issuesCount,
        "totalWarnings" to projectResult.warningsCount,
        "totalInfo" to projectResult.infoCount,
        "totalErrors" to projectResult.errorsCount,
        "totalIgnored" to projectResult.ignoredCount,
      )
    )
  }

  companion object {
    private val GRAPH_ICON = "\uD83D\uDCC9"
  }
}
