package com.jraska.gradle.buildtime

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
  val taskStatistics: TaskStatistics
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
