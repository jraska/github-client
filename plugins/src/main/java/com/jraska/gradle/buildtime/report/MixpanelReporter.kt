package com.jraska.gradle.buildtime.report

import com.jraska.gradle.buildtime.BuildData
import com.jraska.gradle.buildtime.BuildReporter
import com.mixpanel.mixpanelapi.ClientDelivery
import com.mixpanel.mixpanelapi.MessageBuilder
import com.mixpanel.mixpanelapi.MixpanelAPI
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class MixpanelReporter(
  private val apiKey: String,
  private val api: MixpanelAPI
) : BuildReporter {
  override fun report(buildData: BuildData) {
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
    val mixpanelEvent = MessageBuilder(apiKey)
      .event(SINGLE_NAME_FOR_ONE_USER, "Android Build", JSONObject(properties))

    delivery.addMessage(mixpanelEvent)

    api.deliver(delivery)
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
      "gitBranch" to buildData.gitInfo.branchName,
      "gitCommit" to buildData.gitInfo.commitId,
      "gitDirty" to buildData.gitInfo.dirty,
      "gitStatus" to buildData.gitInfo.status,
      "buildDataCollectionOverhead" to buildData.buildDataCollectionOverhead
    ).apply { putAll(buildData.parameters) }
  }

  companion object {
    private val SINGLE_NAME_FOR_ONE_USER = "Build Time Plugin"
    private val STOPWATCH_ICON = "\u23F1"
  }
}
