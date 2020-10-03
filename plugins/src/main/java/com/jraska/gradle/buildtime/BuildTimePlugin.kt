package com.jraska.gradle.buildtime

import com.jraska.gradle.buildtime.report.ConsoleReporter
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildTimePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val buildTimeListener = BuildTimeListener(BuildDataFactory, ConsoleReporter())
    project.gradle.addBuildListener(buildTimeListener)
  }
}
