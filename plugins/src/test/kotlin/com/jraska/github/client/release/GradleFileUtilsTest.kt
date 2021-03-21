package com.jraska.github.client.release

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class GradleFileUtilsTest {
  val GRADLE_FILE_TEXT = """android {
  compileSdkVersion 30

  defaultConfig {
    applicationId "com.jraska.github.client"
    minSdkVersion 24
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
  fun versionNameFoundProperly() {
    val versionName = GradleFileUtils.versionName(GRADLE_FILE_TEXT)

    assertThat(versionName).isEqualTo("0.20.9")
  }

  @Test
  fun versionCodeIsIncremented() {
    val incrementedContent = GradleFileUtils.incrementVersionCode(GRADLE_FILE_TEXT)
    assertThat(incrementedContent).contains("versionCode 67")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length)

    val anotherIncrementedContent = GradleFileUtils.incrementVersionCode(incrementedContent)
    assertThat(anotherIncrementedContent).contains("versionCode 68")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length)
  }

  @Test
  fun patchIsIncremented() {
    val incrementedContent = GradleFileUtils.incrementVersionNamePatch(GRADLE_FILE_TEXT)
    assertThat(incrementedContent).contains("versionName '0.20.10'")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length + 1)

    val anotherIncrementedContent = GradleFileUtils.incrementVersionNamePatch(incrementedContent)
    assertThat(anotherIncrementedContent).contains("versionName '0.20.11'")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length + 1)
  }

  @Test
  fun minorIsIncremented() {
    val incrementedContent = GradleFileUtils.incrementVersionNameMinor(GRADLE_FILE_TEXT)
    assertThat(incrementedContent).contains("versionName '0.21.0'")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length)

    val anotherIncrementedContent = GradleFileUtils.incrementVersionNameMinor(incrementedContent)
    assertThat(anotherIncrementedContent).contains("versionName '0.22.0'")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length)
  }

  @Test
  fun majorIsIncremented() {
    val incrementedContent = GradleFileUtils.incrementVersionNameMajor(GRADLE_FILE_TEXT)
    assertThat(incrementedContent).contains("versionName '1.0.0'")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length - 1)

    val anotherIncrementedContent = GradleFileUtils.incrementVersionNameMajor(incrementedContent)
    assertThat(anotherIncrementedContent).contains("versionName '2.0.0'")
    assertThat(incrementedContent.length).isEqualTo(GRADLE_FILE_TEXT.length - 1)
  }
}
