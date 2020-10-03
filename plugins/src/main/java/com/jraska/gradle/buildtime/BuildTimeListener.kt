package com.jraska.gradle.buildtime

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

internal class BuildTimeListener(
  private val buildDataFactory: BuildDataFactory,
  private val buildReporter: BuildReporter
) : BuildListener {
  override fun buildStarted(gradle: Gradle) = Unit
  override fun settingsEvaluated(gradle: Settings) = Unit
  override fun projectsLoaded(gradle: Gradle) = Unit
  override fun projectsEvaluated(gradle: Gradle) = Unit

  override fun buildFinished(result: BuildResult) {
    val buildData = buildDataFactory.buildData(result)
    buildReporter.report(buildData)
  }
}
