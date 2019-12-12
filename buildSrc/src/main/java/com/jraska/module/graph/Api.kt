package com.jraska.module.graph

object Api {
  object Tasks {
    const val GENERATE_GRAPHVIZ = "generateModulesGrapvizText"

    const val ASSERT_ALL = "assertModulesGraph"
    const val ASSERT_MAX_HEIGHT = "assertMaxHeight"
    const val ASSERT_LAYER_ORDER = "assertModuleLayersOrder"
    const val ASSERT_NO_IN_LAYER_PREFIX = "assertNoDependenciesWithin"
  }

  const val CHECK_TASK = "check"

  const val EXTENSION_ROOT = "moduleGraphRules"
}

