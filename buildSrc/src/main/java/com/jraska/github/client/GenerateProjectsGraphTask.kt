package com.jraska.github.client

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class GenerateProjectsGraphTask : DefaultTask() {
  @TaskAction
  fun run() {
    val modulesTree = DependencyTreeFactory.create(project)
    println(modulesTree.toGraphviz())
  }
}
