package com.jraska.github.client.firebase.report

import com.jraska.github.client.firebase.TestOutcome
import com.jraska.github.client.firebase.TestResult
import com.jraska.github.client.firebase.TestSuiteResult
import com.jraska.gradle.git.GitInfo
import groovy.util.Node
import groovy.util.NodeList
import groovy.util.XmlParser

class FirebaseResultExtractor(
  val firebaseUrl: String,
  val gitInfo: GitInfo,
  val device: String
) {
  fun extract(xml: String): TestSuiteResult {
    val testSuiteNode = XmlParser().parseText(xml)

    val testsCount = testSuiteNode.attributeInt("tests")
    val flakyTests = testSuiteNode.attributeInt("flakes")
    val ignoredCount = testSuiteNode.attributeInt("skipped")
    val failedCount = testSuiteNode.attributeInt("failures")
    val errorsCount = testSuiteNode.attributeInt("errors")
    val time = testSuiteNode.attributeDouble("time")
    val passedCount = testsCount - ignoredCount - failedCount - errorsCount

    val tests = (testSuiteNode.get("testcase") as NodeList)
      .map { it as Node }
      .filter { it.attributeString("name") != "null" }
      .map { parseTestResult(it) }

    val suitePassed = errorsCount == 0 && failedCount == 0

    return TestSuiteResult(
      testResults = tests,
      time = time,
      testsCount = testsCount,
      device = device,
      gitInfo = gitInfo,
      firebaseUrl = firebaseUrl,
      errorsCount = errorsCount,
      passedCount = passedCount,
      failedCount = failedCount,
      flakyCount = flakyTests,
      ignoredCount = ignoredCount,
      suitePassed = suitePassed
    )
  }

  private fun parseTestResult(testNode: Node): TestResult {
    val flaky = testNode.attributeBoolean("flaky")
    val failure = ((testNode.get("failure") as NodeList?)?.firstOrNull() as Node?)?.text()

    val outcome = when {
      flaky -> TestOutcome.FLAKY
      failure != null -> TestOutcome.FAILED
      else -> TestOutcome.PASSED
    }

    val methodName = testNode.attributeString("name")
    val className = testNode.attributeString("classname")
    return TestResult(
      methodName = methodName,
      className = className,
      time = testNode.attributeDouble("time"),
      failure = failure,
      outcome = outcome,
      firebaseUrl = firebaseUrl,
      gitInfo = gitInfo,
      device = device,
      fullName = "$className#$methodName"
    )
  }

  private fun Node.attributeInt(name: String): Int {
    return attribute(name)?.toString()?.toInt() ?: 0
  }

  private fun Node.attributeDouble(name: String): Double {
    return attribute(name).toString().toDouble()
  }

  private fun Node.attributeString(name: String): String {
    return attribute(name).toString()
  }

  private fun Node.attributeBoolean(name: String): Boolean {
    return attribute(name)?.toString()?.toBoolean() ?: false
  }
}
