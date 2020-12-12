package com.jraska.module.extract

import com.jraska.module.FileType
import com.jraska.module.FileTypeStatistics
import com.jraska.module.ModuleArtifactDependency
import com.jraska.module.ModuleStatistics
import com.jraska.module.ModuleType
import com.jraska.module.ProjectStatistics
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class StatisticsGradleExtractor() {
  private val typesToLookFor = arrayOf(FileType.JAVA, FileType.KOTLIN, FileType.XML)

  fun extract(project: Project): ProjectStatistics {
    val moduleStats = project.rootProject
      .subprojects
      .filter { it.childProjects.isEmpty() }
      .map { extractFromModule(it) }

    val externalDependencies = project.rootProject
      .subprojects
      .filter { it.childProjects.isEmpty() }
      .flatMap { extractDependencies(it) }

    return ProjectStatistics(
      modulesCount = moduleStats.size,
      apiModules = moduleStats.count { it.type == ModuleType.Api },
      appModules = moduleStats.count { it.type == ModuleType.App },
      implementationModules = moduleStats.count { it.type == ModuleType.Implementation },
      moduleStatistics = moduleStats,
      externalDependencies = externalDependencies,
      prodKotlinTotalLines = moduleStats
        .map { module -> module.containedProdFiles.single { it.type == FileType.KOTLIN }.lineCount }
        .sum(),

      prodJavaTotalLines = moduleStats
        .map { module -> module.containedProdFiles.single { it.type == FileType.JAVA }.lineCount }
        .sum(),

      prodXmlTotalLines = moduleStats
        .map { module -> module.containedProdFiles.single { it.type == FileType.XML }.lineCount }
        .sum()
    )
  }

  private fun extractDependencies(module: Project): List<ModuleArtifactDependency> {
    val configurationsToLook = setOf("api", "implementation", "testImplementation", "androidTestImplementation", "kapt", "kaptAndroidTest")

    val moduleType = moduleType(module)
    val moduleName = module.name

    return module.configurations
      .filter { configurationsToLook.contains(it.name) }
      .flatMap { configuration ->
        configuration.dependencies
          .filterIsInstance<DefaultExternalModuleDependency>()
          .map {
            ModuleArtifactDependency(
              moduleName = moduleName,
              type = moduleType,
              group = it.group!!,
              configurationName = configuration.name,
              artifact = it.name,
              version = it.version,
              fullName = "${it.group}:${it.name}:${it.version}"
            )
          }
      }.distinct()
  }

  private fun extractFromModule(module: Project): ModuleStatistics {
    val prodFileStats = typesToLookFor.map { getFileTypeStatistics(it, File(module.projectDir, "src/main")) }
    val unitTestFileStats = typesToLookFor.map { getFileTypeStatistics(it, File(module.projectDir, "src/test")) }
    val androidTestFileStats = typesToLookFor.map { getFileTypeStatistics(it, File(module.projectDir, "src/androidTest")) }

    return ModuleStatistics(module.name, prodFileStats, unitTestFileStats, androidTestFileStats, moduleType(module))
  }

  private fun moduleType(module: Project): ModuleType {
    return when {
      module.name.endsWith("-api") -> ModuleType.Api
      module.name.startsWith("app") -> ModuleType.App
      else -> ModuleType.Implementation
    }
  }

  private fun getFileTypeStatistics(type: FileType, src: File): FileTypeStatistics {
    var fileCount = 0
    val lineCount = src.walkBottomUp()
      .filter { it.name.endsWith(type.suffix) }
      .onEach { fileCount++ }
      .map { countLines(it) }
      .sum()

    return FileTypeStatistics(lineCount, fileCount, type)
  }

  private fun countLines(file: File): Int {
    var lines = 0
    val reader = BufferedReader(FileReader(file))
    try {
      while (reader.readLine() != null) lines++
      reader.close()
    } finally {
      reader.close()
    }
    return lines
  }
}
