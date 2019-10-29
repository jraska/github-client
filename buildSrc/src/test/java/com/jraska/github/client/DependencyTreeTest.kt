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

    assert(dependencyTree.longestPath("app").nodeNames == listOf("app", "feature", "lib", "core"))
  }

  @Test
  fun findsProperRoot() {
    val dependencyTree = DependencyTree()

    dependencyTree.addEdge("feature", "lib")
    dependencyTree.addEdge("lib", "core")
    dependencyTree.addEdge("app", "feature")
    dependencyTree.addEdge("app", "lib")
    dependencyTree.addEdge("app", "core")

    assert(dependencyTree.findRoot().key == "app")
  }

  @Test
  fun createSubtreeProperly() {
    val dependencyTree = DependencyTree()

    dependencyTree.addEdge("feature", "lib")
    dependencyTree.addEdge("lib", "core")
    dependencyTree.addEdge("app", "feature")
    dependencyTree.addEdge("feature", "core")
    dependencyTree.addEdge("app", "core")

    val subTree = dependencyTree.subTree("feature")

    assert(subTree.findRoot().key == "feature")
    assert(subTree.heightOf("feature") == 2)
    assert(subTree.longestPath("feature").nodeNames == listOf("feature", "lib", "core"))
  }
}
