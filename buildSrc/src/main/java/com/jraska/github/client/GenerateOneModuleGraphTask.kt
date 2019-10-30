package com.jraska.github.client

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class GenerateOneModuleGraphTask : DefaultTask() {
  @Input
  lateinit var moduleName: String

  @TaskAction
  fun run() {
    val allModulesTree = DependencyTreeFactory.create(project)
    val moduleTree = allModulesTree.subTree(moduleName)

    println(moduleTree.statistics())
    println(GraphvizGenerator.toGraphviz(moduleTree))
  }
}
