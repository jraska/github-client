package com.jraska.github.client.release

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.ByteArrayOutputStream

class ReleasePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.afterEvaluate {
      addTasks(it)
    }
  }

  private fun addTasks(project: Project) {
    project.tasks.register("createNewReleaseAndBumpVersion") {
      it.doFirst {
        CreateReleaseAndBump.execute(project)
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
}
