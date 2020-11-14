package com.jraska.gradle.git

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

object GitInfoProvider {
  fun gitInfo(project: Project): GitInfo {
    val branchName = project.execCommand("git rev-parse --abbrev-ref HEAD")
    val commitId = project.execCommand("git rev-parse --verify HEAD")
    val status = project.execCommand("git status --porcelain")
    val dirty = status.isNotEmpty()

    return GitInfo(branchName, commitId, dirty, status)
  }

  private fun Project.execCommand(command: String): String {
    val output = ByteArrayOutputStream()
    project.exec {
      it.commandLine = command.split(" ")
      it.standardOutput = output
    }

    return output.toString()
  }
}
