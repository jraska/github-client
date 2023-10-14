package com.jraska.appsize.diff

import com.jraska.appsize.RulerJsonParserTest.Companion.reporterFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AppSizeDifferTest {
  @Test
  fun calculatesDiffProperly() {
    val base = reporterFile("appsize/ruler_report_short.json")
    val branch = reporterFile("appsize/ruler_report_short_for_diff.json")

    val appSizeDiff = AppSizeDiffer.calculateDiff(base, branch)

    assertThat(appSizeDiff.sizeDiff.downloadSizeBytes).isEqualTo(625_379L)
    assertThat(appSizeDiff.sizeDiff.installSizeBytes).isEqualTo(1_486_412L)

    assertThat(appSizeDiff.addedComponents[0].name)
      .isEqualTo("io.sentry:sentry-android-ndk:7.0.0-beta.1")
    assertThat(appSizeDiff.addedComponents[0].size.downloadSizeBytes).isEqualTo(386_299L)

    assertThat(appSizeDiff.removedComponents[0].name)
      .isEqualTo("com.facebook.fresco:nativeimagetranscoder:2.6.0")
    assertThat(appSizeDiff.removedComponents[0].size.installSizeBytes).isEqualTo(451_030L)

    assertThat(appSizeDiff.componentDiffs[0].sizeDiff.downloadSizeBytes).isEqualTo(-699L)
    assertThat(appSizeDiff.componentDiffs[0].sizeDiff.installSizeBytes).isEqualTo(-682L)
  }

  @Test
  fun noDiffOnIdenticalReport() {
    val base = reporterFile("appsize/ruler_report_short.json")
    val branch = reporterFile("appsize/ruler_report_short.json")

    val appSizeDiff = AppSizeDiffer.calculateDiff(base, branch)

    assertThat(appSizeDiff.sizeDiff.downloadSizeBytes).isEqualTo(0L)
    assertThat(appSizeDiff.sizeDiff.installSizeBytes).isEqualTo(0L)
    assertThat(appSizeDiff.componentDiffs).isEmpty()
    assertThat(appSizeDiff.addedComponents).isEmpty()
    assertThat(appSizeDiff.removedComponents).isEmpty()
  }
}
