package com.jraska.github.client.firebase.report

import com.jraska.github.client.firebase.Device
import com.jraska.github.client.firebase.TestOutcome
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class FirebaseOutputParserTest {
  @Test
  fun findsUrlProperly() {
    val url = FirebaseOutputParser.parseUrl(EXAMPLE_OUTPUT)

    assertThat(url).isEqualTo("https://console.firebase.google.com/project/github-client-25b47/testlab/histories/bh.45e06288a93d3fad/matrices/4937539158600939569")
  }

  @Test
  fun findsNewUrlProperly() {
    val url = FirebaseOutputParser.parseUrl(EXAMPLE_OUTPUT_2)

    assertThat(url).isEqualTo("https://console.firebase.google.com/project/github-client-25b47/testlab/histories/bh.45e06288a93d3fad/matrices/4992084868422917621")
  }

  @Test
  fun parsesFailedResultProperly() {
    val deviceResults = FirebaseOutputParser.deviceResults(listOf(Device.Pixel6a), EXAMPLE_OUTPUT)

    assertThat(deviceResults).hasSize(1)
    assertThat(deviceResults[0].outcome).isEqualTo(TestOutcome.FAILED)
  }

  @Test
  fun parsesSuccessResultProperly() {
    val deviceResults =
      FirebaseOutputParser.deviceResults(listOf(Device.Pixel6a, Device.Pixel7), EXAMPLE_OUTPUT_2)

    assertThat(deviceResults).hasSize(2)
    assertThat(deviceResults[0].outcome).isEqualTo(TestOutcome.PASSED)
    assertThat(deviceResults[1].outcome).isEqualTo(TestOutcome.PASSED)
  }

  @Test
  fun parsesFlakyResultProperly() {
    val deviceResults = FirebaseOutputParser.deviceResults(
      listOf(Device.Pixel6a, Device.Pixel2, Device.Pixel7),
      FLAKY_OUTPUT
    )

    assertThat(deviceResults).hasSize(3)
    assertThat(deviceResults[0].outcome).isEqualTo(TestOutcome.FAILED)
    assertThat(deviceResults[1].outcome).isEqualTo(TestOutcome.FLAKY)
    assertThat(deviceResults[2].outcome).isEqualTo(TestOutcome.PASSED)
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
      ┌─────────┬────────────────────────┬────────────────────────────────┐
      │ OUTCOME │   TEST_AXIS_VALUE      │          TEST_DETAILS          │
      ├─────────┼────────────────────────┼────────────────────────────────┤
      │ Failed  │ bluejay-32-en-portrait │ 1 test cases failed, 13 passed │
      └─────────┴────────────────────────┴────────────────────────────────┘
    """.trimIndent()

    val EXAMPLE_OUTPUT_2 = """
      Have questions, feedback, or issues? Get support by visiting:
        https://firebase.google.com/support/

      Uploading [/home/runner/work/github-client/github-client/app/build/outputs/apk/debug/app-debug.apk] to Firebase Test Lab...
      Uploading [/home/runner/work/github-client/github-client/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk] to Firebase Test Lab...
      Raw results will be stored in your GCS bucket at [https://console.developers.google.com/storage/browser/test-lab-twsawhz0hy5am-h35y3vymzadax/2022-12-06T19:01:26.459081/]

      Test [matrix-zvqc95opgthaa] has been created in the Google Cloud.
      Creating individual test executions...
      ...............................done.
      Firebase Test Lab will execute your instrumentation test on 2 device(s). More devices may be added later if flaky test attempts are specified.

      Test results will be streamed to [ https://console.firebase.google.com/project/github-client-25b47/testlab/histories/bh.45e06288a93d3fad/matrices/4992084868422917621 ].

      19:02:41 Test matrix status: Pending:2 
      19:02:53 Test matrix status: Pending:2 
      19:03:05 Test matrix status: Running:2 
      19:03:18 Test matrix status: Running:2 
      19:03:30 Test matrix status: Running:2 
      19:03:42 Test matrix status: Running:2 
      19:03:54 Test matrix status: Finished:1 Running:1 
      19:04:06 Test matrix status: Finished:2           
      Instrumentation testing complete.

      More details are available at [ https://console.firebase.google.com/project/github-client-25b47/testlab/histories/bh.45e06288a93d3fad/matrices/4992084868422917621 ].
      ┌─────────┬────────────────────────┬──────────────────────┐
      │ OUTCOME │    TEST_AXIS_VALUE     │     TEST_DETAILS     │
      ├─────────┼────────────────────────┼──────────────────────┤
      │ Passed  │ bluejay-32-en-portrait │ 16 test cases passed │
      │ Passed  │ panther-33-en-portrait │ 16 test cases passed │
      └─────────┴────────────────────────┴──────────────────────┘
    """.trimIndent()
  }

  val FLAKY_OUTPUT = """
    Test results will be streamed to [ https://console.firebase.google.com/project/github-client-25b47/testlab/histories/bh.45e06288a93d3fad/matrices/7826067465486884652 ].

    17:54:09 Test matrix status: Pending:1 
    17:54:22 Test matrix status: Running:1 
    17:54:34 Test matrix status: Running:1 
    17:54:46 Test matrix status: Running:1 
    17:54:58 Test matrix status: Running:1 
    17:55:10 Test matrix status: Finished:1 Pending:1 
    17:55:22 Test matrix status: Finished:1 Pending:1 
    17:55:34 Test matrix status: Finished:1 Running:1 
    17:55:47 Test matrix status: Finished:1 Running:1 
    17:55:59 Test matrix status: Finished:1 Running:1 
    17:56:11 Test matrix status: Finished:1 Running:1 
    17:56:23 Test matrix status: Finished:2           
    Instrumentation testing complete.

    More details are available at [ https://console.firebase.google.com/project/github-client-25b47/testlab/histories/bh.45e06288a93d3fad/matrices/7826067465486884652 ].
    ┌─────────┬────────────────────────┬───────────────────────────────┐
    │ OUTCOME │    TEST_AXIS_VALUE     │          TEST_DETAILS         │
    ├─────────┼────────────────────────┼───────────────────────────────┤
    │ Failed  │ bluejay-32-en-portrait │ 16 test cases passed          │ 
    │ Flaky   │ walleye-27-en-portrait │ 1 test cases flaky, 15 passed │
    | Passed  │ panther-33-en-portrait │ 16 test cases passed          │
    └─────────┴────────────────────────┴───────────────────────────────┘
  """.trimIndent()
}
