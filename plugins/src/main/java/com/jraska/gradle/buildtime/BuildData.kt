package com.jraska.gradle.buildtime

import com.jraska.gradle.git.GitInfo

data class BuildData(
  val action: String,
  val buildTime: Long,
  val tasks: List<String>,
  val failed: Boolean,
  val failure: Throwable?,
  val daemonsRunning: Int,
  val thisDaemonBuilds: Int,
  val hostname: String,
  val gradleVersion: String,
  val operatingSystem: String,
  val environment: Environment,
  val parameters: Map<String, Any>,
  val gitInfo: GitInfo,
  val taskStatistics: TaskStatistics,
  val buildDataCollectionOverhead: Long // This is only collection overhead. We cannot see the reporting overhead as measuring and reporting it will add meta- overhead :D
)

enum class Environment {
  IDE,
  CI,
  CMD
}

data class TaskStatistics(
  val total: Int,
  val upToDate: Int,
  val fromCache: Int,
  val executed: Int
)
