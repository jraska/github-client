package com.jraska.gradle.buildtime

import com.jraska.gradle.buildtime.report.ConsoleReporter
import com.jraska.gradle.buildtime.report.MixpanelReporter
import com.mixpanel.mixpanelapi.MixpanelAPI
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.sql.DriverManager.println

class BuildTimePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val buildTimeListener = BuildTimeListener(BuildDataFactory, reporter())
    project.gradle.addBuildListener(buildTimeListener)
  }

  private fun reporter(): BuildReporter {
    val mixpanelToken: String? = System.getenv("GITHUB_CLIENT_MIXPANEL_API_KEY")
    if (mixpanelToken == null) {
      println("'GITHUB_CLIENT_MIXPANEL_API_KEY' not set, data will be reported to console only")
      return ConsoleReporter()
    } else {
      return MixpanelReporter(mixpanelToken, MixpanelAPI())
    }
  }
}
