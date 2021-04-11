package com.jraska.github.client.about

import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView

import androidx.test.platform.app.InstrumentationRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class VersionInfoModelTest {
  @Test
  fun bindsProperly() {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val frameLayout = FrameLayout(context)

    val versionInfoModel = VersionInfoModel()
    val view = LayoutInflater.from(context).inflate(versionInfoModel.layout, frameLayout)
    versionInfoModel.bind(view)

    assertThat(view.findViewById<TextView>(R.id.version_name_and_code).text).contains("Version")
  }
}
