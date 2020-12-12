package com.jraska.module

import com.jraska.analytics.AnalyticsReporter
import com.jraska.module.extract.StatisticsGradleExtractor
import org.gradle.api.Project

class GradleStatisticsReportProcess(
  private val statisticsGradleExtractor: StatisticsGradleExtractor,
  private val moduleStatsReporter: ModuleStatsReporter
) {
  fun executeReport(project: Project) {
    val extract = statisticsGradleExtractor.extract(project)
    moduleStatsReporter.report(extract)
  }

  companion object {
    fun create(): GradleStatisticsReportProcess {
      return GradleStatisticsReportProcess(StatisticsGradleExtractor(), reporter())
    }

    private fun reporter() = ModuleStatsReporter(AnalyticsReporter.create("Module Stats Plugin"))
  }
}

