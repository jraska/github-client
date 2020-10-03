package com.jraska.gradle.buildtime

interface BuildReporter {
  fun report(buildData: BuildData)
}
