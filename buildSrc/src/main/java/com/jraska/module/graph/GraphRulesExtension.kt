package com.jraska.module.graph

open class GraphRulesExtension {
  var appModuleName = ":app"
  var maxHeight: Int = 4
  var moduleLayersFromTheTop = arrayOf(":feature", ":lib", ":core")
  var notAllowedInLayerDependencies = arrayOf(":feature", ":lib")
}
