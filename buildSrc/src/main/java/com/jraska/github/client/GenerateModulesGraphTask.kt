package com.jraska.github.client

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class GenerateModulesGraphTask : DefaultTask() {
  @Input
  var layers: Array<String> = emptyArray()

  @TaskAction
  fun run() {
    val allModulesTree = DependencyTreeFactory.create(project)

    println(allModulesTree.statistics())
    println(GraphvizGenerator.toGraphviz(allModulesTree, layers.toSet()))
  }
}
