package com.jraska.module.extract

import com.jraska.module.ArtifactDependencyType
import com.jraska.module.FileType
import com.jraska.module.FileTypeStatistics
import com.jraska.module.ModuleArtifactDependency
import com.jraska.module.ModuleMetadata
import com.jraska.module.ModuleStatistics
import com.jraska.module.ModuleType
import com.jraska.module.ProjectStatistics
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.ResolvedDependency
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.LinkedList
import java.util.Queue

class StatisticsGradleExtractor {
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
      apiModules = moduleStats.count { it.metadata.type == ModuleType.Api },
      appModules = moduleStats.count { it.metadata.type == ModuleType.App },
      implementationModules = moduleStats.count { it.metadata.type == ModuleType.Implementation },
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
    val metadata = module.moduleMetadata()
    val dependencies = module.configurations
      .filter { CONFIGURATION_TO_LOOK.containsKey(it.name) }
      .flatMap { configuration ->
        val projectDependencies = configuration.allDependencies.filterIsInstance(ProjectDependency::class.java)
        configuration.resolvedConfiguration.firstLevelModuleDependencies
          .filter { isExternalDependency(it, projectDependencies) }
          .flatMap { traverseAndAddChildren(it) }
          .map {
            ModuleArtifactDependency(
              metadata = metadata,
              group = it.moduleGroup,
              dependencyType = CONFIGURATION_TO_LOOK.getValue(configuration.name),
              artifact = it.moduleName,
              version = it.moduleVersion,
              fullName = it.name
            )
          }
      }.distinct()

    return dependencies
  }

  private fun traverseAndAddChildren(firstLevelDependency: ResolvedDependency): Set<ResolvedDependency> {
    val queue: Queue<ResolvedDependency> = LinkedList()

    val dependencies = mutableSetOf(firstLevelDependency)
    queue.offer(firstLevelDependency)

    while (queue.isNotEmpty()) {
      val element = queue.poll()
      element.children.forEach { child ->
        dependencies.add(child)
        queue.offer(child)
      }
    }

    return dependencies
  }

  private fun isExternalDependency(dependency: ResolvedDependency, projectDependencies: List<ProjectDependency>): Boolean {
    return projectDependencies.none {
      it.group == dependency.moduleGroup && it.name == dependency.moduleName
    }
  }

  private fun extractFromModule(module: Project): ModuleStatistics {
    val prodFileStats = typesToLookFor.map { getFileTypeStatistics(it, File(module.projectDir, "src/main")) }
    val unitTestFileStats = typesToLookFor.map { getFileTypeStatistics(it, File(module.projectDir, "src/test")) }
    val androidTestFileStats = typesToLookFor.map { getFileTypeStatistics(it, File(module.projectDir, "src/androidTest")) }

    return ModuleStatistics(module.moduleMetadata(), prodFileStats, unitTestFileStats, androidTestFileStats)
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

  companion object {
    val CONFIGURATION_TO_LOOK = mapOf(
      "debugAndroidTestCompileClasspath" to ArtifactDependencyType.AndroidTest,
      "debugCompileClasspath" to ArtifactDependencyType.Compile,
      "releaseCompileClasspath" to ArtifactDependencyType.Compile,
      "debugUnitTestCompileClasspath" to ArtifactDependencyType.Test,
      "compileClasspath" to ArtifactDependencyType.Compile,
      "testCompileClasspath" to ArtifactDependencyType.Test,
      "kapt" to ArtifactDependencyType.Kapt
    )

    fun Project.moduleMetadata(): ModuleMetadata {
      return ModuleMetadata(name, moduleType(this))
    }

    private fun moduleType(module: Project): ModuleType {
      return when {
        module.name.endsWith("-api") -> ModuleType.Api
        module.name.startsWith("app") -> ModuleType.App
        else -> ModuleType.Implementation
      }
    }
  }
}
