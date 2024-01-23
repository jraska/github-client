package com.jraska.github.client.firebase

object GCloudCommands {
  fun firebaseRunCommand(
    testConfiguration: TestConfiguration,
    envVars: Map<String, String>
  ): String {
    if (envVars.isEmpty()) {
      throw IllegalArgumentException("Version without env vars not supported for simplicity now")
    }

    val envVarsString = envVars.entries.map { "${it.key}=${it.value}" }.joinToString(",")

    val devicesString = testConfiguration.devices
      .joinToString(" ") { "--device " + it.firebaseCommandString() }

    return ("gcloud " +
      "firebase test android run " +
      "--app ${testConfiguration.appApkPath} " +
      "--test ${testConfiguration.testApkPath} " +
      "$devicesString " +
      "--num-flaky-test-attempts=0")
  }
}
