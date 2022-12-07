package com.jraska.github.client.firebase

import com.jraska.github.client.firebase.Device.Companion.Pixel6a
import com.jraska.github.client.firebase.Device.Companion.Pixel7Pro
import org.gradle.api.Project
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TestConfiguration(
  val appApkPath: String,
  val testApkPath: String,
  val devices: List<Device>,
  val resultDir: String
) {
  companion object {
    fun create(project: Project): TestConfiguration {
      val appApk = "${project.buildDir}/outputs/apk/debug/app-debug.apk"
      val testApk = "${project.buildDir}/outputs/apk/androidTest/debug/app-debug-androidTest.apk"

      val resultDir = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())
      val devices = listOf(Pixel7Pro, Pixel6a, Device.Pixel2)

      return TestConfiguration(appApk, testApk, devices, resultDir)
    }
  }
}
