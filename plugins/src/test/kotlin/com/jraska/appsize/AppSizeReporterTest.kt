package com.jraska.appsize

import com.jraska.github.client.release.data.RecordingAnalyticsReporter
import com.jraska.gradle.git.GitInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class AppSizeReporterTest {
  private lateinit var appSizeReporter: AppSizeReporter
  private lateinit var analyticsReporter: RecordingAnalyticsReporter

  @Before
  fun setUp() {
    analyticsReporter = RecordingAnalyticsReporter()
    appSizeReporter = AppSizeReporter(
      analyticsReporter, "testDevice",
      GitInfo("aBranch", "id", true, "status")
    )
  }

  @Test
  fun reportsInsightsProperly() {
    val report = appSizeReport()

    appSizeReporter.report(report)

    val events = analyticsReporter.events()

    val appSizeEvent = events[0]

    assertThat(appSizeEvent.properties["appDownloadSizeBytes"]).isEqualTo(2887818L)
    assertThat(appSizeEvent.properties["appInstallSizeBytes"]).isEqualTo(3666902L)
    assertThat(appSizeEvent.properties["allComponentsCount"]).isEqualTo(3)
    assertThat(appSizeEvent.properties["externalComponentsCount"]).isEqualTo(2)
    assertThat(appSizeEvent.properties["internalComponentsCount"]).isEqualTo(1)
    assertThat(appSizeEvent.properties["internalDownloadSize"]).isEqualTo(254063L)
    assertThat(appSizeEvent.properties["internalInstallSize"]).isEqualTo(730853L)
    assertThat(appSizeEvent.properties["externalDownloadSize"]).isEqualTo(444855L)
    assertThat(appSizeEvent.properties["externalInstallSize"]).isEqualTo(712456L)
  }

  @Test
  fun reportsCommonPropertiesProperly() {
    val report = appSizeReport()

    appSizeReporter.report(report)

    val events = analyticsReporter.events()

    assertThat(events).hasSize(4)

    events.forEach {
      assertThat(it.properties["gitBranch"]).isEqualTo("aBranch")
      assertThat(it.properties["referenceDevice"]).isEqualTo("testDevice")
      assertThat(it.properties["appVersion"]).isEqualTo("0.55.1")
    }
  }

  @Test
  fun reportsComponentPropertiesProperly() {
    val report = appSizeReport()

    appSizeReporter.report(report)

    val events = analyticsReporter.events()

    assertThat(events).hasSize(4)

    assertThat(events[1].properties["componentType"]).isEqualTo("EXTERNAL")
    assertThat(events[2].properties["componentName"]).isEqualTo(":app")
    assertThat(events[2].properties["componentType"]).isEqualTo("INTERNAL")
    assertThat(events[2].properties["componentInstallSizeBytes"]).isEqualTo(730853L)
    assertThat(events[3].properties["componentDownloadSizeBytes"]).isEqualTo(183976L)
  }

  private fun appSizeReport(): AppSizeReport {
    val file = RulerJsonParserTest.reporterFile()
    return RulerJsonParser.parse(file)
  }
}
