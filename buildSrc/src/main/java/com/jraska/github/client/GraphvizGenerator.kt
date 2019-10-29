package com.jraska.github.client

object GraphvizGenerator {
  fun toGraphviz(dependencyTree: DependencyTree): String {

    val stringBuilder = StringBuilder()

    stringBuilder.append("digraph G {\n")
    dependencyTree.nodes().flatMap { node -> node.children.map { node.key to it.key } }
      .forEach { (moduleName, dependency) ->
        stringBuilder.append("\"$moduleName\"")
          .append(" -> ")
          .append("\"$dependency\"")
          .append("\n")
      }

    stringBuilder.append("}")

    return stringBuilder.toString()

  }
}
