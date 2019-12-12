package com.jraska.module.graph

import com.jraska.github.client.tasks.AssertLayersOrderTask
import com.jraska.github.client.tasks.AssertModuleTreeHeightTask
import com.jraska.github.client.tasks.AssertNoInLayerDependencies
import com.jraska.github.client.tasks.GenerateModulesGraphTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import java.util.Locale

@Suppress("unused") // Used as plugin
class ModuleGraphAssertionsPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    val graphRules = project.extensions.create("moduleGraphRules", GraphRulesExtension::class.java)

    project.addModuleGraphGeneration()

    val allAssertionsTask = project.tasks.create("assertModulesGraph")
    project.tasks.find { it.name == "check" }?.dependsOn(allAssertionsTask)

    project.addMaxHeightTasks(graphRules).forEach { allAssertionsTask.dependsOn(it) }
    project.addModuleLayersTasks(graphRules).forEach { allAssertionsTask.dependsOn(it) }
    project.addInLayerDependencyTasks(graphRules).forEach { allAssertionsTask.dependsOn(it) }
  }

  private fun Project.addModuleGraphGeneration() {
    tasks.create("generateModulesGrapvizText", GenerateModulesGraphTask::class.java)
  }

  private fun Project.addMaxHeightTasks(graphRules: GraphRulesExtension): List<Task> {
    if (graphRules.maxHeight <= 0) {
      return emptyList()
    }

    val task = tasks.create("assertMaxHeight", AssertModuleTreeHeightTask::class.java)
    task.maxHeight = graphRules.maxHeight
    task.moduleName = graphRules.appModuleName

    return listOf(task)
  }

  private fun Project.addModuleLayersTasks(graphRules: GraphRulesExtension): List<Task> {
    if (graphRules.moduleLayersFromTheTop.isEmpty()) {
      return emptyList()
    }

    val task = tasks.create("assertModuleLayersOrder", AssertLayersOrderTask::class.java)
    task.layersFromTheTop = graphRules.moduleLayersFromTheTop
    return listOf(task)
  }

  private fun Project.addInLayerDependencyTasks(graphRules: GraphRulesExtension): List<Task> {
    return graphRules.notAllowedInLayerDependencies.map { layerPrefix ->
      val taskNameSuffix = layerPrefix.replace(":", "").capitalizeFirst()
      val task = tasks.create("assertNoDependenciesWithin$taskNameSuffix", AssertNoInLayerDependencies::class.java)
      task.layerPrefix = layerPrefix
      return@map task
    }
  }

  private fun String.capitalizeFirst(): String {
    return this.substring(0, 1).toUpperCase(Locale.US).plus(this.substring(1))
  }
}
