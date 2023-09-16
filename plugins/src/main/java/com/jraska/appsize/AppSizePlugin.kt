package com.jraska.appsize

import org.gradle.api.Plugin
import org.gradle.api.Project

class AppSizePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.afterEvaluate { theProject ->
      theProject.tasks.register("reportAppSize") { appSizeReportTask ->
        appSizeReportTask.doLast {
          ReportRulerOutput.report(project)
        }

        appSizeReportTask.dependsOn(project.tasks.named("analyzeReleaseBundle"))
      }
    }
  }

  companion object {
    const val REFERENCE_DEVICE_PROPERTY_NAME = "referenceDevice"
  }
}
