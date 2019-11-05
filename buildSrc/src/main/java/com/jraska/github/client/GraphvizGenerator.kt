package com.jraska.github.client

object GraphvizGenerator {
  fun toGraphviz(dependencyTree: DependencyTree, groups: Set<String> = emptySet()): String {

    val longestPathConnections = dependencyTree.longestPath()
      .nodeNames.zipWithNext()
      .toSet()

    val stringBuilder = StringBuilder()

    stringBuilder.append("digraph G {\n")

    groups.forEach {
      stringBuilder.append(generateGroup(dependencyTree, it))
    }

    dependencyTree.nodes().flatMap { node -> node.children.map { node.key to it.key } }
      .forEach { connection ->
        stringBuilder.append("\"${connection.first}\"")
          .append(" -> ")
          .append("\"${connection.second}\"")

        if (longestPathConnections.contains(connection)) {
          stringBuilder.append(" [color=red style=bold]")
        }

        stringBuilder.append("\n")
      }

    stringBuilder.append("}")

    return stringBuilder.toString()
  }

  private fun generateGroup(dependencyTree: DependencyTree, groupName: String): String {
    val builder = StringBuilder()
      .append("subgraph cluster_").append(groupName.replace(":", "")).appendln("{")
      .appendln("style = filled;")
      .appendln("color = lightgrey;")
      .appendln("node[style = filled, color = white];")
      .append("label = \"").append(groupName).appendln("\"")

    dependencyTree.nodes().filter { it.key.startsWith(groupName) }.forEach {
      builder.append("\"").append(it.key).appendln("\"")
    }

    return builder.appendln("}")
      .appendln()
      .toString()
  }
}
