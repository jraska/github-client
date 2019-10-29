package com.jraska.github.client

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

open class AssertModuleTreeHeight : DefaultTask() {
  lateinit var moduleName: String
  var maxHeight: Int = 0

  @TaskAction
  fun run() {
    val modulesTree = DependencyTreeFactory.create(project)

    val height = modulesTree.heightOf(moduleName)
    if (height > maxHeight) {
      val longestPath = modulesTree.longestPath(moduleName)
      throw GradleException("Module $moduleName is allowed to have maximum height of $maxHeight, but has $height, problematic dependencies: ${longestPath.pathString()}")
    }
  }
}
