package com.jraska.gradle.buildtime

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.tasks.execution.statistics.TaskExecutionStatisticsEventAdapter
import org.gradle.api.invocation.Gradle
import org.gradle.internal.event.ListenerManager
import org.gradle.internal.time.Clock
import org.gradle.invocation.DefaultGradle

internal class BuildTimeListener(
  private val buildDataFactory: BuildDataFactory,
  private val buildReporter: BuildReporter
) : BuildListener {
  private val taskExecutionStatisticsEventAdapter = TaskExecutionStatisticsEventAdapter()
  private var configuredTime: Long = 0

  override fun settingsEvaluated(gradle: Settings) = Unit
  override fun projectsLoaded(gradle: Gradle) = Unit
  override fun projectsEvaluated(gradle: Gradle) {
    val listenerManager = (gradle as DefaultGradle).services[ListenerManager::class.java]
    listenerManager.addListener(taskExecutionStatisticsEventAdapter)

    configuredTime = gradle.services[Clock::class.java].currentTime
  }

  override fun buildFinished(result: BuildResult) {
    val buildData = buildDataFactory.buildData(result, taskExecutionStatisticsEventAdapter.statistics, configuredTime)

    println("Build data collected in ${buildData.buildDataCollectionOverhead} ms")

    buildReporter.report(buildData)
  }

}
