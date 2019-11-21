package com.jraska.github.client.graph

import org.junit.Test

class DependencyGraphTest {
  @Test
  fun correctHeightIsMaintained() {
    val dependencyTree = DependencyGraph.create(
      listOf(
        "app" to "feature",
        "app" to "lib",
        "feature" to "lib",
        "lib" to "core"
      )
    )

    assert(dependencyTree.heightOf("app") == 3)
  }

  @Test
  fun findsProperLongestPath() {
    val dependencyTree = DependencyGraph.create(
      listOf(
        "app" to "feature",
        "app" to "lib",
        "app" to "core",
        "feature" to "lib",
        "lib" to "core"
      )
    )

    assert(dependencyTree.longestPath("app").nodeNames == listOf("app", "feature", "lib", "core"))
  }

  @Test
  fun findsProperRoot() {
    val dependencyTree = DependencyGraph.create(
      listOf(
        "feature" to "lib",
        "lib" to "core",
        "app" to "feature",
        "app" to "lib",
        "app" to "core"
      )
    )

    assert(dependencyTree.findRoot().key == "app")
  }

  @Test
  fun createSubtreeProperly() {
    val dependencyTree = DependencyGraph.create(
      listOf(
        "feature" to "lib",
        "lib" to "core",
        "app" to "feature",
        "feature" to "core",
        "app" to "core"
      )
    )

    val subTree = dependencyTree.subTree("feature")

    assert(subTree.findRoot().key == "feature")
    assert(subTree.heightOf("feature") == 2)
    assert(subTree.longestPath("feature").nodeNames == listOf("feature", "lib", "core"))
  }

  @Test
  fun createCountStatisticsWell() {
    val dependencyTree = DependencyGraph.create(
      listOf(
        "feature" to "lib",
        "lib" to "core",
        "app" to "feature",
        "feature" to "core",
        "app" to "core"
      )
    )

    val statistics = dependencyTree.statistics()

    assert(statistics.height == 3)
    assert(statistics.modulesCount == 4)
    assert(statistics.edgesCount == 5)
  }
}
