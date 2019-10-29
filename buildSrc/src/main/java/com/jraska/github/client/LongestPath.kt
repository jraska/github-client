package com.jraska.github.client

class LongestPath(
  val nodeNames: List<String>
) {
  fun pathString(): String {
    return nodeNames.joinToString(" -> ")
  }
}
