package com.jraska.lint

import org.gradle.api.Plugin
import org.gradle.api.Project

class LintReporterPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.afterEvaluate {
      it.tasks.register("lintStatisticsReport") { lintReportTask ->
        lintReportTask.doLast {
          val result = LintProjectExtractor().extract(lintReportTask.project)

          println(result)
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
