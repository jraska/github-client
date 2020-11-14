package com.jraska.github.client.firebase

interface TestResultReporter {
  fun report(results: TestSuiteResult)
}
