package com.jraska.github.client

import org.junit.Test

class DependencyTreeTest {
  @Test
  fun correctHeightIsMaintained() {
    val dependencyTree = DependencyTree()

    dependencyTree.addEdge("app", "feature")
    assert(dependencyTree.heightOf("app") == 1)

    dependencyTree.addEdge("app", "lib")
    assert(dependencyTree.heightOf("app") == 1)

    dependencyTree.addEdge("feature", "lib")
    dependencyTree.addEdge("lib", "core")

    assert(dependencyTree.heightOf("app") == 3)
  }

  @Test
  fun findsProperLongestPath() {
    val dependencyTree = DependencyTree()

    dependencyTree.addEdge("app", "feature")
    dependencyTree.addEdge("app", "lib")
    dependencyTree.addEdge("app", "core")
    dependencyTree.addEdge("feature", "lib")
    dependencyTree.addEdge("lib", "core")

    assert(dependencyTree.longestPath("app").nodeNames.equals(listOf("app", "feature", "lib", "core")))
  }
}
