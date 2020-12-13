package com.jraska.lint

import com.jraska.module.ModuleMetadata

data class LintIssue(
  val metadata: ModuleMetadata,
  val id: String,
  val message: String,
  val severity: Severity,
  val category: String,
  val priority: Int,
  val summary: String,
  val errorLine: String?,
  val location: String?
)

data class LintModuleResult(
  val metadata: ModuleMetadata,
  val issues: List<LintIssue>,
  val issuesCount: Int,
  val errorsCount: Int,
  val warningsCount: Int,
  val infoCount: Int,
  val ignoredCount: Int
)

data class LintProjectResult(
  val issuesCount: Int,
  val errorsCount: Int,
  val warningsCount: Int,
  val infoCount: Int,
  val ignoredCount: Int,
  val moduleResults: List<LintModuleResult>
)

enum class Severity {
  Error,
  Warning,
  Info,
  Fatal,
  Ignore
}
