package com.jraska.github.client.firebase.report

import com.jraska.github.client.firebase.TestResult
import com.jraska.github.client.firebase.TestResultReporter
import com.jraska.github.client.firebase.TestSuiteResult
import com.mixpanel.mixpanelapi.ClientDelivery
import com.mixpanel.mixpanelapi.MessageBuilder
import com.mixpanel.mixpanelapi.MixpanelAPI
import org.json.JSONObject

class MixpanelTestResultsReporter(
  private val apiKey: String,
  private val api: MixpanelAPI
) : TestResultReporter {
  override fun report(results: TestSuiteResult) {
    val delivery = ClientDelivery()

    val properties = convertTestSuite(results)
    val messageBuilder = MessageBuilder(apiKey)
    val moduleEvent = messageBuilder
      .event(SINGLE_NAME_FOR_TEST_REPORTS_USER, "Android Test Suite Firebase", JSONObject(properties))

    delivery.addMessage(moduleEvent)

    results.testResults.forEach {
      val testProperties = convertSingleTest(it)

      val estateEvent = messageBuilder.event(SINGLE_NAME_FOR_TEST_REPORTS_USER, "Android Test Firebase", JSONObject(testProperties))
      delivery.addMessage(estateEvent)
    }

    api.deliver(delivery)

    println("$FLAG_ICON Test result reported to Mixpanel $FLAG_ICON")
  }

  private fun convertSingleTest(testResult: TestResult): Map<String, Any?> {
    return mutableMapOf<String, Any?>(
      "className" to testResult.className,
      "methodName" to testResult.methodName,
      "device" to testResult.device,
      "firebaseUrl" to testResult.firebaseUrl,
      "fullName" to testResult.fullName,
      "failure" to testResult.failure,
      "outcome" to testResult.outcome,
      "testTime" to testResult.time
    ).apply { putAll(testResult.gitInfo.asAnalyticsProperties()) }
  }

  private fun convertTestSuite(results: TestSuiteResult): Map<String, Any?> {
    return mutableMapOf<String, Any?>(
      "passed" to results.suitePassed,
      "suiteTime" to results.time,
      "device" to results.device,
      "firebaseUrl" to results.firebaseUrl,
      "passedCount" to results.passedCount,
      "testsCount" to results.testsCount,
      "ignoredCount" to results.ignoredCount,
      "flakyCount" to results.flakyCount,
      "failedCount" to results.failedCount,
      "errorsCount" to results.errorsCount
    ).apply { putAll(results.gitInfo.asAnalyticsProperties()) }
  }

  companion object {
    private val FLAG_ICON = "\uD83C\uDFC1"
    private val SINGLE_NAME_FOR_TEST_REPORTS_USER = "Test Reporter"
  }
}
