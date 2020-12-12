package com.jraska.module

import com.jraska.analytics.AnalyticsEvent
import com.jraska.analytics.AnalyticsReporter

class ModuleStatsReporter(
  private val analyticsReporter: AnalyticsReporter
) {
  fun report(stats: ProjectStatistics) {
    val events = mutableListOf<AnalyticsEvent>()

    val globalProperties = convertModules(stats)
    events.add(AnalyticsEvent("Android Modules", globalProperties))

    stats.moduleStatistics.forEach {
      val properties = convertSingleModule(it)
      events.add(AnalyticsEvent("Android Module Detail", properties))
    }

    stats.externalDependencies.forEach {
      val properties = convertModuleDependency(it)
      events.add(AnalyticsEvent("Android Module External Dependency", properties))
    }

    analyticsReporter.report(*events.toTypedArray())

    println("$GRAPH_ICON Module stats reported to Mixpanel $GRAPH_ICON")
  }

  private fun convertModules(stats: ProjectStatistics): Map<String, Any?> {
    return mapOf<String, Any?>(
      "moduleCount" to stats.modulesCount,
      "apiModules" to stats.apiModules,
      "appModules" to stats.appModules,
      "implementationModules" to stats.implementationModules,
      "prodKotlinLines" to stats.prodKotlinTotalLines,
      "prodJavaLines" to stats.prodJavaTotalLines,
      "prodXmlLines" to stats.prodXmlTotalLines,
    )
  }

  private fun convertSingleModule(moduleStats: ModuleStatistics): Map<String, Any?> {
    val properties = mutableMapOf<String, Any?>(
      "moduleName" to moduleStats.moduleName,
      "moduleType" to moduleStats.type.name,
    )

    properties.addFilesData(moduleStats.containedProdFiles, "prod")
    properties.addFilesData(moduleStats.containedAndroidTestFiles, "androidTest")
    properties.addFilesData(moduleStats.containedUnitTestFiles, "unitTest")

    return properties
  }

  private fun MutableMap<String, Any?>.addFilesData(moduleStats: Collection<FileTypeStatistics>, sourceSet: String) {
    moduleStats.forEach {
      val type = typeProperty(it.type)
      this["${sourceSet}${type}Lines"] = it.lineCount
      this["${sourceSet}${type}Files"] = it.fileCount
    }
  }

  private fun typeProperty(type: FileType): String {
    return when (type) {
      FileType.KOTLIN -> "Kotlin"
      FileType.JAVA -> "Java"
      FileType.XML -> "Xml"
    }
  }

  private fun convertModuleDependency(artifactDependency: ModuleArtifactDependency): Map<String, Any?> {
    return mapOf<String, Any?>(
      "moduleName" to artifactDependency.moduleName,
      "moduleType" to artifactDependency.type.name,
      "groupId" to artifactDependency.group,
      "artifactId" to artifactDependency.artifact,
      "version" to artifactDependency.version,
      "fullComponentName" to artifactDependency.fullName,
      "dependencyType" to artifactDependency.dependencyType
    )
  }

  companion object {
    private val GRAPH_ICON = "\uD83D\uDCC9"
  }
}
