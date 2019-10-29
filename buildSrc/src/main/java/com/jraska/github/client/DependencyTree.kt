package com.jraska.github.client

class DependencyTree() {
  private val nodes = mutableMapOf<String, Node>()

  fun longestPath(key: String): LongestPath {
    val nodeNames = nodes.getValue(key)
      .longestPath()
      .map { it.key }

    return LongestPath(nodeNames)
  }

  fun heightOf(key: String): Int {
    return nodes.getValue(key).height()
  }

  fun addEdge(from: String, to: String) {
    getOrCreate(from).children.add(getOrCreate(to))
  }

  fun toGraphviz(): String {
    val stringBuilder = StringBuilder()

    stringBuilder.append("digraph G {\n")
    nodes.values.flatMap { node -> node.children.map { node.key to it.key } }
      .forEach { (moduleName, dependency) ->
        stringBuilder.append("\"$moduleName\"")
          .append(" -> ")
          .append("\"$dependency\"")
          .append("\n")
      }

    stringBuilder.append("}")

    return stringBuilder.toString()
  }

  private fun getOrCreate(key: String): Node {
    return nodes[key] ?: Node(key).also { nodes[key] = it }
  }

  class Node(val key: String) {
    val children = mutableSetOf<Node>()

    private fun isLeaf() = children.isEmpty()

    fun height(): Int {
      if (isLeaf()) {
        return 0
      } else {
        return 1 + children.map { it.height() }.max()!!
      }
    }

    internal fun longestPath(): List<Node> {
      if (isLeaf()) {
        return listOf(this)
      } else {
        val path = mutableListOf<Node>(this)

        val maxHeightNode = children.maxBy { it.height() }!!
        path.addAll(maxHeightNode.longestPath())

        return path
      }
    }
  }
}
