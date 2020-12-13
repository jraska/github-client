package com.jraska.lint

import com.jraska.analytics.AnalyticsReporter
import org.gradle.api.Project

class LintReportProcess(
  private val lintProjectExtractor: LintProjectExtractor,
  private val lintAnalyticsReporter: LintAnalyticsReporter
) {
  fun executeReport(project: Project) {
    val report = lintProjectExtractor.extract(project)
    lintAnalyticsReporter.report(report)
  }

  companion object {
    fun create(): LintReportProcess {
      return LintReportProcess(
        LintProjectExtractor(),
        LintAnalyticsReporter(AnalyticsReporter.create("Lint Reporter"))
      )
    }
  }
}
