package com.jraska.github.client.graph

class LongestPath(
  val nodeNames: List<String>
) {
  fun pathString(): String {
    return nodeNames.joinToString(" -> ")
  }
}
