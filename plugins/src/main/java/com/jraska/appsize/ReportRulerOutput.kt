package com.jraska.appsize

import com.jraska.analytics.AnalyticsReporter
import com.jraska.gradle.git.GitInfoProvider
import org.gradle.api.Project

object ReportRulerOutput {
  fun report(project: Project) {
    val rulerJsonFile = project.layout.buildDirectory
      .file("reports/ruler/release/report.json").get().asFile

    if (!rulerJsonFile.exists()) {
      throw IllegalStateException("Ruler JSON file not found in $rulerJsonFile")
    }

    val appSizeReport = RulerJsonParser.parse(rulerJsonFile)

    val gitInfo = GitInfoProvider.gitInfo(project)
    val referenceDevice = referenceDevice(project)
    val analyticsReporter = AnalyticsReporter.create("App Size Reporter")

    AppSizeReporter(analyticsReporter, referenceDevice, gitInfo).report(appSizeReport)
  }

  private fun referenceDevice(project: Project): String {
    if (project.hasProperty(AppSizePlugin.REFERENCE_DEVICE_PROPERTY_NAME)) {
      return project.property(AppSizePlugin.REFERENCE_DEVICE_PROPERTY_NAME) as String
    } else {
      return "referenceDeviceSetByRuler"
    }
  }
}
