package com.jraska.lint

import com.jraska.module.extract.StatisticsGradleExtractor.Companion.moduleMetadata
import org.gradle.api.Project
import java.io.File

class LintProjectExtractor {
  fun extract(project: Project): LintProjectResult {
    val moduleStats = project.rootProject
      .subprojects
      .filter { it.childProjects.isEmpty() }
      .map { extractFromModule(it) }

    return LintProjectResult(
      moduleResults = moduleStats,
      issuesCount = moduleStats.map { it.issuesCount }.sum(),
      warningsCount = moduleStats.map { it.warningsCount }.sum(),
      infoCount = moduleStats.map { it.infoCount }.sum(),
      errorsCount = moduleStats.map { it.errorsCount }.sum(),
      ignoredCount = moduleStats.map { it.ignoredCount }.sum()
    )
  }

  private fun extractFromModule(module: Project): LintModuleResult {
    val moduleMetadata = module.moduleMetadata()

    val xml = locateLintFile(module).readText()
    val issues = LintXmlParser(moduleMetadata).parse(xml)

    return LintModuleResult(
      metadata = moduleMetadata,
      issues = issues,
      issuesCount = issues.size,
      warningsCount = issues.count { it.severity == Severity.Warning },
      errorsCount = issues.count { it.severity == Severity.Error || it.severity == Severity.Fatal },
      ignoredCount = issues.count { it.severity == Severity.Ignore },
      infoCount = issues.count { it.severity == Severity.Info }
    )
  }

  private fun locateLintFile(project: Project): File {
    val buildDir = project.buildDir
    val androidModuleCase = File(buildDir, "reports/lint-results.xml")
    if (androidModuleCase.exists()) {
      return androidModuleCase
    }

    val jvmModuleCase = File(buildDir, "test-results/lint-results.xml")
    if (jvmModuleCase.exists()) {
      return jvmModuleCase
    }

    throw IllegalStateException("Lint results didn't found in paths: [$androidModuleCase, $jvmModuleCase]")
  }
}
