package com.jraska.github.client

object GraphvizGenerator {
  fun toGraphviz(dependencyTree: DependencyTree): String {

    val longestPathConnections = dependencyTree.longestPath()
      .nodeNames.zipWithNext()
      .toSet()

    val stringBuilder = StringBuilder()

    stringBuilder.append("digraph G {\n")
    dependencyTree.nodes().flatMap { node -> node.children.map { node.key to it.key } }
      .forEach { connection ->
        stringBuilder.append("\"${connection.first}\"")
          .append(" -> ")
          .append("\"${connection.second}\"")

        if(longestPathConnections.contains(connection)) {
          stringBuilder.append(" [color=red style=bold]")
        }

        stringBuilder.append("\n")
      }

    stringBuilder.append("}")

    return stringBuilder.toString()

  }
}
