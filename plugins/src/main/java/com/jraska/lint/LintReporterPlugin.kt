package com.jraska.lint

import org.gradle.api.Plugin
import org.gradle.api.Project

class LintReporterPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.afterEvaluate { theProject ->
      theProject.tasks.register("lintStatisticsReport") { lintReportTask ->
        lintReportTask.doLast {
          LintReportProcess.create().executeReport(lintReportTask.project)
        }

        project.rootProject.subprojects
          .filter { it.childProjects.isEmpty() }
          .forEach {
            lintReportTask.dependsOn(it.tasks.named("lint"))
          }
      }
    }
  }
}
