package com.jraska.github.client.tasks

import com.jraska.github.client.GradleDependencyGraphFactory
import com.jraska.github.client.graph.DependencyGraph
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class AssertNoInLayerDependencies : DefaultTask() {
  @Input
  lateinit var layerPrefix: String

  @TaskAction
  fun run() {
    val modulesTree = GradleDependencyGraphFactory.create(project)

    val inLayerDependencies = modulesTree.nodes()
      .filter { it.key.startsWith(layerPrefix) }
      .ifEmpty { throw GradleException("There are no modules with prefix $layerPrefix") }
      .flatMap { parent -> parent.dependsOn.map { dependency -> parent to dependency } }
      .filter { it.second.key.startsWith(layerPrefix) }

    if (inLayerDependencies.isNotEmpty()) {
      throw GradleException(buildErrorMessage(inLayerDependencies))
    }
  }

  private fun buildErrorMessage(inLayerDependencies: List<Pair<DependencyGraph.Node, DependencyGraph.Node>>): String {
    val errorsMessage = inLayerDependencies.joinToString("\n") { " Module '${it.first.key}' cannot depend on '${it.second.key}'." }
    val suggestion = "Try using interface modules to share break this dependency"

    return "Dependencies within '$layerPrefix' are not allowed. The not allowed dependencies are: \n$errorsMessage\n$suggestion"
  }
}
