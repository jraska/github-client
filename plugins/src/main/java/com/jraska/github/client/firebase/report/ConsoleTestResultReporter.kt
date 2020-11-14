package com.jraska.github.client.firebase.report

import com.jraska.github.client.firebase.TestResultReporter
import com.jraska.github.client.firebase.TestSuiteResult

class ConsoleTestResultReporter : TestResultReporter {
  override fun report(results: TestSuiteResult) {
    println(results)
  }
}
