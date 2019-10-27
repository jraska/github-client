package com.jraska.github.client

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency
import org.gradle.api.tasks.TaskAction

open class GenerateProjectsGraphTask : DefaultTask() {
  @TaskAction
  fun run() {
    val configurationToLook = setOf("implementation", "api")

    val dependencies = project.subprojects
      .flatMap { project ->
        project.configurations
          .filter { configurationToLook.contains(it.name) }
          .flatMap { configuration ->
            configuration.dependencies.filterIsInstance(DefaultProjectDependency::class.java)
              .map { it.dependencyProject }
          }
          .map { project.moduleDisplayName() to it.moduleDisplayName() }
      }

    printInGraphvizFormat(dependencies)
  }

  private fun printInGraphvizFormat(dependencyGraph: List<Pair<String, String>>) {
    val stringBuilder = StringBuilder()

    stringBuilder.append("digraph G {\n")
    dependencyGraph.forEach { (moduleName, dependency) ->
      stringBuilder.append("\"$moduleName\"")
        .append(" -> ")
        .append("\"$dependency\"")
        .append("\n")
    }
    stringBuilder.append("}")

    println(stringBuilder.toString())
  }

  private fun Project.moduleDisplayName(): String {
    return displayName.replace("project", "")
      .replace("'", "")
      .trim()
  }
}
