package com.jraska.appsize.diff

import com.jraska.appsize.AppSize
import com.jraska.appsize.AppSizeReport
import com.jraska.appsize.Component

data class AppSizeDiff(
  val base: AppSizeReport,
  val branch: AppSizeReport,
  val sizeDiff: AppSize,
  val componentDiffs: List<ComponentDiff>,
  val addedComponents: List<Component>,
  val removedComponents: List<Component>
)

class ComponentDiff(
  val sizeDiff: AppSize,
  val base: Component,
  val branch: Component
) {
  fun diffName(): String {
    if(base.name == branch.name) {
      return base.name
    } else {
      val commonPrefix = base.key

      val baseNameSuffix = base.name.substring(commonPrefix.length)
      val branchNameSuffix = branch.name.substring(commonPrefix.length)

      return "$commonPrefix($baseNameSuffix -> $branchNameSuffix)"
    }
  }
}
