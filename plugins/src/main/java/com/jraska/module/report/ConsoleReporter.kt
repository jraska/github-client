package com.jraska.module.report

import com.jraska.module.ModuleStatsReporter
import com.jraska.module.ProjectStatistics

class ConsoleReporter : ModuleStatsReporter {
  override fun report(stats: ProjectStatistics) {
    println(stats)
  }
}
