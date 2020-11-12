package com.jraska.module

interface ModuleStatsReporter {
  fun report(stats: ProjectStatistics)
}
