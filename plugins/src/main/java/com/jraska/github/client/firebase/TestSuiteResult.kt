package com.jraska.github.client.firebase

data class TestSuiteResult(
  val testResults: List<TestResult>,
  val time: Double,
  val suitePassed: Boolean,
  val testsCount: Int,
  val failedCount: Int,
  val errorsCount: Int,
  val passedCount: Int,
  val ignoredCount: Int,
  val flakyCount: Int,
  val device: String
)

data class TestResult(
  val outcome: TestOutcome,
  val className: String,
  val methodName: String,
  val time: Double,
  val fullName: String,
  val failure: String?,
  val device: String
)

enum class TestOutcome {
  PASSED,
  FAILED,
  FLAKY
}
