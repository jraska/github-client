package com.jraska.appsize

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File

class RulerJsonParserTest {
  @Test
  fun deserializesProperly() {
    val file = reporterFile()
    val report = RulerJsonParser.parse(file)

    assertThat(report).usingRecursiveComparison().isEqualTo(expectedReport())
  }

  private fun expectedReport(): AppSizeReport {
    return AppSizeReport(
      name = "com.jraska.github.client",
      AppSize(
        downloadSizeBytes = 2887818,
        installSizeBytes = 3666902,
      ),
      version = "0.55.1",
      variant = "release",
      components = listOf(
        Component(
          "com.google.android.material:material:1.9.0",
          ComponentType.EXTERNAL,
          AppSize(260879, 261426)
        ),
        Component(":app", ComponentType.INTERNAL, AppSize(254063, 730853)),
        Component(
          "com.facebook.fresco:nativeimagetranscoder:2.6.0",
          ComponentType.EXTERNAL,
          AppSize(183976, 451030)
        )
      )
    )
  }

  companion object {
    fun reporterFile() = reporterFile("appsize/ruler_report_short.json")

    fun reporterFile(path: String): File {
      val uri = RulerJsonParserTest::class.java.classLoader.getResource(path)
      return File(uri?.path!!)
    }
  }
}
