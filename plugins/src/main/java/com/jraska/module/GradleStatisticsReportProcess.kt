package com.jraska.module

import com.jraska.module.extract.StatisticsGradleExtractor
import com.jraska.module.report.ConsoleReporter
import com.jraska.module.report.MixpanelModuleStatsReporter
import com.mixpanel.mixpanelapi.MixpanelAPI
import org.gradle.api.Project
import java.sql.DriverManager

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

    private fun reporter(): ModuleStatsReporter {
      val mixpanelToken: String? = System.getenv("GITHUB_CLIENT_MIXPANEL_API_KEY")
      if (mixpanelToken == null) {
        DriverManager.println("'GITHUB_CLIENT_MIXPANEL_API_KEY' not set, data will be reported to console only")
        return ConsoleReporter()
      } else {
        return MixpanelModuleStatsReporter(mixpanelToken, MixpanelAPI())
      }
    }
  }
}
