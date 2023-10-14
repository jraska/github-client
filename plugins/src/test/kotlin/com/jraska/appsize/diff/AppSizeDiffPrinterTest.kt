package com.jraska.appsize.diff

import com.jraska.appsize.RulerJsonParserTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AppSizeDiffPrinterTest {
  @Test
  fun testPrintsExpectedOutput() {
    val base = RulerJsonParserTest.reporterFile("appsize/ruler_report_short.json")
    val branch = RulerJsonParserTest.reporterFile("appsize/ruler_report_short_for_diff.json")

    val appSizeDiff = AppSizeDiffer.calculateDiff(base, branch)
    val readableString = AppSizeDiffPrinter.asReadableString(appSizeDiff)

    assertThat(readableString).contains("Download: 625.38 kB, Install: 1.49 MB")

    assertThat(readableString).contains("+ io.sentry:sentry-android-ndk:7.0.0-beta.1")
    assertThat(readableString).contains("Download: 386.3 kB, Install: 1.23 MB")

    assertThat(readableString).contains("- com.facebook.fresco:nativeimagetranscoder:2.6.0")
    assertThat(readableString).contains("Download: 183.98 kB, Install: 451.03 kB")

    assertThat(readableString).contains(":app")
    assertThat(readableString).contains("Download: -699 B, Install: -682 B")
  }
}
