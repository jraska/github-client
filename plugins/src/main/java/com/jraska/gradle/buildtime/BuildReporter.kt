package com.jraska.gradle.buildtime

import com.jraska.analytics.AnalyticsEvent
import com.jraska.analytics.AnalyticsReporter
import com.mixpanel.mixpanelapi.ClientDelivery
import java.util.concurrent.TimeUnit

class BuildReporter(
  private val analyticsReporter: AnalyticsReporter
) {
  fun report(buildData: BuildData) {
    try {
      reportToMixpanel(buildData)
    } catch (ex: Exception) {
      println("Build time reporting failed: $ex")
    }
  }

  private fun reportToMixpanel(buildData: BuildData) {
    val start = nowMillis()

    reportInternal(buildData)

    val reportingOverhead = nowMillis() - start
    println("$STOPWATCH_ICON Build time '${buildData.buildTime} ms' reported to Mixpanel in $reportingOverhead ms.$STOPWATCH_ICON")
  }

  private fun reportInternal(buildData: BuildData) {
    val delivery = ClientDelivery()

    val properties = convertBuildData(buildData)
    val event = AnalyticsEvent("Android Build", properties)
    analyticsReporter.report(event)
  }

  private fun nowMillis() = TimeUnit.NANOSECONDS.toMillis(System.nanoTime())

  private fun convertBuildData(buildData: BuildData): Map<String, Any?> {
    return mutableMapOf<String, Any?>(
      "action" to buildData.action,
      "buildTime" to buildData.buildTime,
      "tasks" to buildData.tasks.joinToString(),
      "failed" to buildData.failed,
      "failure" to buildData.failure.toString(),
      "daemonsRunning" to buildData.daemonsRunning,
      "thisDaemonBuilds" to buildData.thisDaemonBuilds,
      "hostname" to buildData.hostname,
      "gradleVersion" to buildData.gradleVersion,
      "OS" to buildData.operatingSystem,
      "environment" to buildData.environment,
      "tasksTotal" to buildData.taskStatistics.total,
      "tasksUpToDate" to buildData.taskStatistics.upToDate,
      "tasksFromCache" to buildData.taskStatistics.fromCache,
      "tasksExecuted" to buildData.taskStatistics.executed,
      "buildDataCollectionOverhead" to buildData.buildDataCollectionOverhead
    ).apply {
      putAll(buildData.parameters)
      putAll(buildData.gitInfo.asAnalyticsProperties())
    }
  }

  companion object {
    private val STOPWATCH_ICON = "\u23F1"
  }
}
