package com.jraska.github.client.release

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class ReleasePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.afterEvaluate {
      addTasks(it)
    }
  }

  private fun addTasks(project: Project) {
    project.tasks.register("incrementPatch") {
      it.doFirst {
        updateMinorVersionInBuildGradle(project)
      }
    }
  }

  private fun updateMinorVersionInBuildGradle(project: Project) {
    val buildGradleFile = File(project.projectDir, "build.gradle")
    val buildGradleText = buildGradleFile.readText()

    val incrementVersionCode = GradleFileVersionIncrement.incrementVersionCode(buildGradleText)
    val newContent = GradleFileVersionIncrement.incrementVersionNamePatch(incrementVersionCode)

    buildGradleFile.writeText(newContent)
  }
}
