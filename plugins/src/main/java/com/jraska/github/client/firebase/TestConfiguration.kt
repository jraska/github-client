package com.jraska.github.client.firebase

import org.gradle.api.Project
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TestConfiguration(
  val appApkPath: String,
  val testApkPath: String,
  val firstDevice: Device,
  val secondDevice: Device,
  val resultDir: String
) {
  companion object {
    fun create(project: Project): TestConfiguration {
      val appApk = "${project.buildDir}/outputs/apk/debug/app-debug.apk"
      val testApk = "${project.buildDir}/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
      val firstDevice = Device.Pixel7Pro
      val secondDevice = Device.Pixel6a
      val resultDir = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())

      return TestConfiguration(appApk, testApk, firstDevice, secondDevice, resultDir)
    }
  }
}
