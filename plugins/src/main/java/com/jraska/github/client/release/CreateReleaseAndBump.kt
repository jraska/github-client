package com.jraska.github.client.release

import com.jraska.github.client.release.data.GitHubApiFactory
import org.gradle.api.Project
import java.io.File

object CreateReleaseAndBump {
  fun execute(project: Project) {
    val versionName = versionName(project)
    val environment = Environment.create()

    val api = GitHubApiFactory.create(environment)
    api.createRelease(versionName)
  }

  private fun versionName(project: Project): String {
    val buildGradleFile = File(project.projectDir, "build.gradle")
    val buildGradleText = buildGradleFile.readText()

    return GradleFileUtils.versionName(buildGradleText)
  }

  private fun updatePatchVersionInBuildGradle(project: Project) {
    val buildGradleFile = File(project.projectDir, "build.gradle")
    val buildGradleText = buildGradleFile.readText()

    val incrementVersionCode = GradleFileUtils.incrementVersionCode(buildGradleText)
    val newContent = GradleFileUtils.incrementVersionNamePatch(incrementVersionCode)

    buildGradleFile.writeText(newContent)
  }
}
