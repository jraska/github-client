package com.jraska.github.client.firebase.report

import com.jraska.analytics.AnalyticsEvent
import com.jraska.analytics.AnalyticsReporter
import com.jraska.github.client.firebase.TestResult
import com.jraska.github.client.firebase.TestSuiteResult
import com.jraska.gradle.CiInfo
import com.jraska.gradle.git.GitInfo

class TestResultsReporter(
  private val analyticsReporter: AnalyticsReporter,
  private val firebaseUrl: String,
  private val gitInfo: GitInfo,
  private val ciInfo: CiInfo?,
) {
  fun report(results: TestSuiteResult) {
    val delivery = mutableListOf<AnalyticsEvent>()

    val properties = convertTestSuite(results)
    delivery.add(AnalyticsEvent("Android Test Suite Firebase", properties))

    results.testResults.forEach {
      val testProperties = convertSingleTest(it)
      delivery.add(AnalyticsEvent("Android Test Firebase", testProperties))
    }

    analyticsReporter.report(*delivery.toTypedArray())

    println("$FLAG_ICON Test result reported to Mixpanel $FLAG_ICON")
  }

  private fun convertSingleTest(testResult: TestResult): Map<String, Any?> {
    return mutableMapOf<String, Any?>(
      "className" to testResult.className,
      "methodName" to testResult.methodName,
      "device" to testResult.device,
      "firebaseUrl" to firebaseUrl,
      "fullName" to testResult.fullName,
      "failure" to testResult.failure,
      "outcome" to testResult.outcome,
      "testTime" to testResult.time
    ).apply {
      putAll(gitInfo.asAnalyticsProperties())
      if (ciInfo != null) {
        putAll(ciInfo.asAnalyticsProperties())
      }
    }
  }

  private fun convertTestSuite(results: TestSuiteResult): Map<String, Any?> {
    return mutableMapOf<String, Any?>(
      "passed" to results.suitePassed,
      "suiteTime" to results.time,
      "device" to results.device,
      "firebaseUrl" to firebaseUrl,
      "passedCount" to results.passedCount,
      "testsCount" to results.testsCount,
      "ignoredCount" to results.ignoredCount,
      "flakyCount" to results.flakyCount,
      "failedCount" to results.failedCount,
      "errorsCount" to results.errorsCount
    ).apply {
      putAll(gitInfo.asAnalyticsProperties())
      if (ciInfo != null) {
        putAll(ciInfo.asAnalyticsProperties())
      }
    }
  }

  companion object {
    private const val FLAG_ICON = "\uD83C\uDFC1"
  }
}
