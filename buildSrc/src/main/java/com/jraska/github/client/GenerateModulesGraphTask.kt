package com.jraska.github.client

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class GenerateModulesGraphTask : DefaultTask() {
  lateinit var moduleName: String

  @TaskAction
  fun run() {
    val allModulesTree = DependencyTreeFactory.create(project)
    val moduleTree = allModulesTree.subTree(moduleName)

    println(GraphvizGenerator.toGraphviz(moduleTree))
  }
}
