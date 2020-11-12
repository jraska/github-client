package com.jraska.module.report

import com.jraska.module.FileType
import com.jraska.module.FileTypeStatistics
import com.jraska.module.ModuleStatistics
import com.jraska.module.ModuleStatsReporter
import com.jraska.module.ProjectStatistics
import com.mixpanel.mixpanelapi.ClientDelivery
import com.mixpanel.mixpanelapi.MessageBuilder
import com.mixpanel.mixpanelapi.MixpanelAPI
import org.json.JSONObject

class MixpanelModuleStatsReporter(
  private val apiKey: String,
  private val api: MixpanelAPI
) : ModuleStatsReporter {
  override fun report(stats: ProjectStatistics) {
    reportToMixpanel(stats)
  }

  private fun reportToMixpanel(stats: ProjectStatistics) {
    val delivery = ClientDelivery()

    val properties = convertModules(stats)
    val messageBuilder = MessageBuilder(apiKey)
    val moduleEvent = messageBuilder
      .event(SINGLE_NAME_FOR_STATS_USER, "Android Modules", JSONObject(properties))

    delivery.addMessage(moduleEvent)

    stats.moduleStatistics.forEach {
      val estateProperty = convertSingleModule(it)

      val estateEvent = messageBuilder.event(SINGLE_NAME_FOR_STATS_USER, "Android Module Detail", JSONObject(estateProperty))
      delivery.addMessage(estateEvent)
    }

    api.deliver(delivery)

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

  companion object {
    private val SINGLE_NAME_FOR_STATS_USER = "Module Stats Plugin"
    private val GRAPH_ICON = "\uD83D\uDCC9"
  }
}
