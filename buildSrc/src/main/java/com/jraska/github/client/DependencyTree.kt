package com.jraska.github.client

class DependencyTree() {
  private val nodes = mutableMapOf<String, Node>()

  fun findRoot(): Node {
    require(nodes.isNotEmpty()) { "Dependency Tree is empty" }

    val rootCandidates = nodes().toMutableSet()

    nodes().flatMap { it.children }
      .forEach { rootCandidates.remove(it) }

    return rootCandidates.associateBy { heightOf(it.key) }
      .maxBy { it.key }!!.value
  }

  fun nodes(): Collection<Node> = nodes.values

  fun longestPath(): LongestPath {
    return longestPath(findRoot().key)
  }

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

  fun subTree(key: String): DependencyTree {
    val dependencyTree = DependencyTree()

    addConnections(nodes.getValue(key), dependencyTree)

    return dependencyTree
  }

  private fun addConnections(node: Node, into: DependencyTree) {
    node.children.forEach {
      into.addEdge(node.key, it.key)
      addConnections(it, into)
    }
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
