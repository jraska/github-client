package com.jraska.github.client.release

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class VersionIncrementTest {
  val GRADLE_FILE_TEXT = """android {
  compileSdkVersion 30

  defaultConfig {
    applicationId "com.jraska.github.client"
    minSdkVersion 21
    targetSdkVersion 30
    versionName '0.20.9'
    versionCode 66
    multiDexEnabled true

    testInstrumentationRunner "com.jraska.github.client.TestRunner"
  }

  adbOptions {
    installOptions "-g"
  }

  compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
  }

  kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}"""

  @Test
  fun versionCodeIsIncremented() {
    val incrementedContent = GradleFileVersionIncrement.incrementVersionCode(GRADLE_FILE_TEXT)
    assertThat(incrementedContent).contains("versionCode 67")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length)

    val anotherIncrementedContent = GradleFileVersionIncrement.incrementVersionCode(incrementedContent)
    assertThat(anotherIncrementedContent).contains("versionCode 68")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length)
  }

  @Test
  fun patchIsIncremented() {
    val incrementedContent = GradleFileVersionIncrement.incrementVersionNamePatch(GRADLE_FILE_TEXT)
    assertThat(incrementedContent).contains("versionName '0.20.10'")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length + 1)

    val anotherIncrementedContent = GradleFileVersionIncrement.incrementVersionNamePatch(incrementedContent)
    assertThat(anotherIncrementedContent).contains("versionName '0.20.11'")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length + 1)
  }
}
