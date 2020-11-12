package com.jraska.module

import org.gradle.api.Plugin
import org.gradle.api.Project

class ModuleStatsPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.afterEvaluate {
      it.tasks.register("reportModuleStatistics") { task ->
        task.doLast {
          GradleStatisticsReportProcess.create().executeReport(task.project)
        }
      }
    }
  }
}
