package com.jraska.github.client.tasks

import com.jraska.github.client.GradleDependencyGraphFactory
import com.jraska.github.client.graph.GraphvizWriter
import com.jraska.github.client.graph.KotlinCodeWriter
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class GenerateModulesGraphTask : DefaultTask() {
  @Input
  var layers: Array<String> = emptyArray()

  @TaskAction
  fun run() {
    val allModulesTree = GradleDependencyGraphFactory.create(project)

    println(allModulesTree.statistics())
    println(GraphvizWriter.toGraphviz(allModulesTree, layers.toSet()))
    println(KotlinCodeWriter.toKotlinCode(allModulesTree))
  }
}
