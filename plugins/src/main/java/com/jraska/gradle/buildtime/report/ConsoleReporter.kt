package com.jraska.gradle.buildtime.report

import com.jraska.gradle.buildtime.BuildData
import com.jraska.gradle.buildtime.BuildReporter

class ConsoleReporter : BuildReporter {
  override fun report(buildData: BuildData) {
    println(buildData)
  }
}
