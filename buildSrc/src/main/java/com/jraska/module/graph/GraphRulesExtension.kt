package com.jraska.module.graph

open class GraphRulesExtension {
  var appModuleName = ":app"
  var maxHeight: Int = 0
  var moduleLayersFromTheTop = emptyArray<String>()
  var restrinctInLayerDependencies = emptyArray<String>()
}
