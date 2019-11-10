package com.jraska.github.client

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class AssertCrossLayerDependencies : DefaultTask() {
  @Input
  lateinit var layersFromTheTop: Array<String>

  @TaskAction
  fun run() {
    val modulesTree = DependencyTreeFactory.create(project)

    verifyAllLayersHaveModule(modulesTree)

    val againstLayerDependencies = modulesTree.nodes()
      .flatMap { parent -> parent.dependsOn.map { dependency -> parent to dependency } }
      .filter { isRestrictedDependency(it) }

    if (againstLayerDependencies.isNotEmpty()) {
      throw GradleException(buildErrorMessage(againstLayerDependencies))
    }
  }

  private fun buildErrorMessage(againstLayerDependencies: List<Pair<DependencyGraph.Node, DependencyGraph.Node>>): String {
    val errorsMessage = againstLayerDependencies.joinToString("\n") { " Module '${it.first.key}' cannot depend on '${it.second.key}'." }

    return "Dependencies agaist direction of layers '${layersDependencyString()}' are not allowed. The violating dependencies are: \n$errorsMessage"
  }

  private fun layersDependencyString(): String {
    return layersFromTheTop.joinToString(" -> ") // for example: ":feature -> :lib -> :core"
  }

  private fun verifyAllLayersHaveModule(modulesTree: DependencyGraph) {
    val nodes = modulesTree.nodes()

    for (layerPrefix in layersFromTheTop) {
      val someNodeInLayer = nodes.find { it.key.startsWith(layerPrefix) }
      if (someNodeInLayer == null) {
        throw GradleException("There is no module, which belongs to layer '$layerPrefix'")
      }
    }
  }

  private fun isRestrictedDependency(dependency: Pair<DependencyGraph.Node, DependencyGraph.Node>): Boolean {
    val higherLayerIndex = layerIndex(dependency.first.key) ?: return false
    val lowerLayerIndex = layerIndex(dependency.second.key) ?: return false

    return higherLayerIndex > lowerLayerIndex
  }

  private fun layerIndex(moduleName: String): Int? {
    for ((index, layerPrefix) in layersFromTheTop.withIndex()) {
      if (moduleName.startsWith(layerPrefix)) {
        return index
      }
    }

    return null
  }
}
