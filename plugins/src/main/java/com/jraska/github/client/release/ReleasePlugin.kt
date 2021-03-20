package com.jraska.github.client.release

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.ByteArrayOutputStream
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
        updatePatchVersionInBuildGradle(project)
      }
    }

    project.tasks.register("markAllPrsWithReleaseMilestone") {
      it.doFirst {
        val latestTag = project.latestTag()
        ReleaseMarksPRs.execute(latestTag)
      }
    }
  }

  private fun Project.latestTag(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    exec {
      it.commandLine("git describe --tags --abbrev=0".split(" "))
      it.standardOutput = byteArrayOutputStream
    }

    return byteArrayOutputStream.toString().trim() // trim() is there is it returned linefeed	- %0a
  }

  private fun updatePatchVersionInBuildGradle(project: Project) {
    val buildGradleFile = File(project.projectDir, "build.gradle")
    val buildGradleText = buildGradleFile.readText()

    val incrementVersionCode = GradleFileVersionIncrement.incrementVersionCode(buildGradleText)
    val newContent = GradleFileVersionIncrement.incrementVersionNamePatch(incrementVersionCode)

    buildGradleFile.writeText(newContent)
  }
}
