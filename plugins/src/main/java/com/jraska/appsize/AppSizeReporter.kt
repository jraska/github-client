package com.jraska.appsize

import com.jraska.analytics.AnalyticsEvent
import com.jraska.analytics.AnalyticsReporter
import com.jraska.appsize.ComponentType.EXTERNAL
import com.jraska.appsize.ComponentType.INTERNAL
import com.jraska.gradle.git.GitInfo

class AppSizeReporter(
  private val analyticsReporter: AnalyticsReporter,
  private val referenceDevice: String,
  private val gitInfo: GitInfo,
) {
  fun report(report: AppSizeReport) {
    val events = mutableListOf<AnalyticsEvent>()

    val globalProperties = convertGlobal(report)
    events.add(AnalyticsEvent("App Size", globalProperties))

    report.components.forEach {
      val properties = convertComponent(it, report)
      events.add(AnalyticsEvent("App Size Component", properties))
    }

    analyticsReporter.report(*events.toTypedArray())
  }

  private fun convertGlobal(report: AppSizeReport): Map<String, Any?> {
    val insights = calculateInsights(report)

    return mutableMapOf<String, Any?>(
      "appDownloadSizeBytes" to report.size.downloadSizeBytes,
      "appInstallSizeBytes" to report.size.installSizeBytes,
      "allComponentsCount" to insights.componentsCount,
      "internalComponentsCount" to insights.internalComponents,
      "externalComponentsCount" to insights.externalComponents,
      "internalDownloadSize" to insights.internalSize.downloadSizeBytes,
      "internalInstallSize" to insights.internalSize.installSizeBytes,
      "externalDownloadSize" to insights.externalSize.downloadSizeBytes,
      "externalInstallSize" to insights.externalSize.installSizeBytes,
    ).apply {
      putAll(commonProperties(report))
    }
  }

  private fun commonProperties(report: AppSizeReport): Map<String, Any?> {
    return mutableMapOf<String, Any?>(
      "appVersion" to report.version,
      "appVariant" to report.variant,
      "appName" to report.name,
      "referenceDevice" to referenceDevice,
    ).apply {
      putAll(gitInfo.asAnalyticsProperties())
    }
  }

  private fun convertComponent(component: Component, report: AppSizeReport): Map<String, Any?> {
    return mutableMapOf<String, Any?>(
      "componentName" to component.name,
      "componentType" to component.type.toString(),
      "componentDownloadSizeBytes" to component.size.downloadSizeBytes,
      "componentInstallSizeBytes" to component.size.installSizeBytes,
    ).apply {
      putAll(commonProperties(report))
    }
  }

  private fun calculateInsights(report: AppSizeReport): AppSizeInsights {
    return AppSizeInsights(
      componentsCount = report.components.count(),
      internalComponents = report.components.count { it.type == INTERNAL },
      externalComponents = report.components.count { it.type == EXTERNAL },
      internalSize = report.components
        .filter { it.type == INTERNAL }.let { components ->
          AppSize(
            components.sumOf { it.size.downloadSizeBytes },
            components.sumOf { it.size.installSizeBytes }
          )
        },
      externalSize = report.components
        .filter { it.type == EXTERNAL }.let { components ->
          AppSize(
            components.sumOf { it.size.downloadSizeBytes },
            components.sumOf { it.size.installSizeBytes }
          )
        }
    )
  }

}

class AppSizeInsights(
  val componentsCount: Int,
  val internalComponents: Int,
  val externalComponents: Int,
  val internalSize: AppSize,
  val externalSize: AppSize
)
