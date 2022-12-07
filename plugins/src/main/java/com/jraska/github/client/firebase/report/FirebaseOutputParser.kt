package com.jraska.github.client.firebase.report

import com.jraska.github.client.firebase.Device
import com.jraska.github.client.firebase.TestOutcome

object FirebaseOutputParser {
  private val urlPattern = """Test results will be streamed to \[([\S ]*)\]""".toPattern()
  private val deviceOutputPattern = """(Passed|Failed|Flaky )  \│ ([a-z0-9-]*)""".toPattern()

  fun parseUrl(output: String): String {
    val matcher = urlPattern.matcher(output)

    matcher.find()
    return matcher.group(1).trim()
  }

  fun deviceResults(devices: List<Device>, output: String): List<DeviceRunOutcome> {
    val matcher = deviceOutputPattern.matcher(output)

    val results = mutableListOf<DeviceRunOutcome>()
    while (matcher.find()) {
      val outcome = mapOutcome(matcher.group(1).trim())

      val deviceText = matcher.group(2)
      val device = (devices.find { it.cloudStoragePath() == deviceText }
        ?: throw IllegalStateException("Not found device $deviceText"))

      results.add(DeviceRunOutcome(device, outcome))
    }

    if (results.size != devices.size) {
      throw IllegalStateException("The input and output devices should have same amount of devices- Input: $devices, results: $results")
    }

    return results
  }

  private fun mapOutcome(text: String): TestOutcome {
    return when (text) {
      "Passed" -> TestOutcome.PASSED
      "Flaky" -> TestOutcome.FLAKY
      "Failed" -> TestOutcome.FAILED
      else -> throw IllegalArgumentException("Unrecognzed text '$text' to parse as test outcome.")
    }
  }
}
