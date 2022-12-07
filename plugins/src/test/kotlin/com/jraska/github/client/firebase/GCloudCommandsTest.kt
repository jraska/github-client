package com.jraska.github.client.firebase

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class GCloudCommandsTest {
  @Test
  fun composesCommandCorrectly() {
    val testConfiguration = TestConfiguration(
      "/hey/hou/app.apk",
      "hey/hou/test-app.apk",
      listOf(Device.Pixel6a, Device.Pixel7Pro),
      "result-path-123.456"
    )

    val firebaseRunCommand =
      GCloudCommands.firebaseRunCommand(testConfiguration, mapOf("FCM_API_KEY" to "35327abc123"))

    assertThat(firebaseRunCommand).isEqualTo(EXPECTED_COMMAND)
  }

  companion object {
    const val EXPECTED_COMMAND =
      "gcloud firebase test android run " +
        "--app /hey/hou/app.apk " +
        "--test hey/hou/test-app.apk " +
        "--device model=bluejay,version=32,locale=en,orientation=portrait " +
        "--device model=cheetah,version=33,locale=en,orientation=portrait " +
        "--results-dir result-path-123.456 --no-performance-metrics --timeout 3m " +
        "--environment-variables FCM_API_KEY=35327abc123"
  }
}
