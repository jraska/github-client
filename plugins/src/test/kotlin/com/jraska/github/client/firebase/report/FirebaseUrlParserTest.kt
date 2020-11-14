package com.jraska.github.client.firebase.report

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class FirebaseUrlParserTest {
  @Test
  fun findsUrlProperly() {
    val url = FirebaseUrlParser.parse(EXAMPLE_OUTPUT)

    assertThat(url).isEqualTo("https://console.firebase.google.com/project/github-client-25b47/testlab/histories/bh.45e06288a93d3fad/matrices/4937539158600939569")
  }

  companion object {
    val EXAMPLE_OUTPUT = """
      Have questions, feedback, or issues? Get support by visiting:
        https://firebase.google.com/support/

      Uploading [/home/circleci/code/app/build/outputs/apk/debug/app-debug.apk] to Firebase Test Lab...
      Uploading [/home/circleci/code/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk] to Firebase Test Lab...
      Raw results will be stored in your GCS bucket at [https://console.developers.google.com/storage/browser/test-lab-twsawhz0hy5am-h35y3vymzadax/2020-11-14T23:02:48.776162/]

      Test [matrix-1vohggy3nox8r] has been created in the Google Cloud.
      Firebase Test Lab will execute your instrumentation test on 1 device(s).
      Creating individual test executions...
      ..............................................done.

      Test results will be streamed to [https://console.firebase.google.com/project/github-client-25b47/testlab/histories/bh.45e06288a93d3fad/matrices/4937539158600939569].
      23:03:51 Test is Pending
      23:04:27 Starting attempt 1.
      23:04:27 Started logcat recording.
      23:04:27 Started crash monitoring.
      23:04:27 Preparing device.
      23:04:27 Test is Running
      23:04:39 Logging in to Google account on device.
      23:04:39 Installing apps.
      23:04:39 Retrieving Pre-Test Package Stats information from the device.
      23:04:39 Retrieving Performance Environment information from the device.
      23:04:39 Started crash detection.
      23:04:39 Started Out of memory detection
      23:04:39 Started video recording.
      23:04:39 Starting instrumentation test.
      23:05:04 Completed instrumentation test.
      23:05:04 Stopped video recording.
      23:05:04 Retrieving Post-test Package Stats information from the device.
      23:05:04 Logging out of Google account on device.
      23:05:04 Stopped crash monitoring.
      23:05:04 Stopped logcat recording.
      23:05:04 Done. Test time = 20 (secs)
      23:05:04 Starting results processing. Attempt: 1
      23:05:16 Completed results processing. Time taken = 6 (secs)
      23:05:16 Test is Finished

      Instrumentation testing complete.

      More details are available at [https://console.firebase.google.com/project/github-client-25b47/testlab/histories/bh.45e06288a93d3fad/matrices/4937539158600939569].
      ┌─────────┬──────────────────────┬────────────────────────────────┐
      │ OUTCOME │   TEST_AXIS_VALUE    │          TEST_DETAILS          │
      ├─────────┼──────────────────────┼────────────────────────────────┤
      │ Failed  │ flame-29-en-portrait │ 1 test cases failed, 13 passed │
      └─────────┴──────────────────────┴────────────────────────────────┘
    """.trimIndent()
  }
}
