package com.jraska.appsize.diff

import com.jraska.appsize.AppSizeReport
import com.jraska.appsize.Component
import com.jraska.appsize.RulerJsonParser
import java.io.File

object AppSizeDiffer {
  fun calculateDiff(base: File, branch: File): AppSizeDiff {
    val baseAppSizeReport = RulerJsonParser.parse(base)
    val branchAppSizeReport = RulerJsonParser.parse(branch)

    return diff(baseAppSizeReport, branchAppSizeReport)
  }

  private fun diff(base: AppSizeReport, branch: AppSizeReport): AppSizeDiff {
    val sizeDiff = branch.size - base.size

    val branchComponents = branch.components.associateBy { it.name }.toMutableMap()

    val componentDiffs = mutableListOf<ComponentDiff>()
    val removedComponents = mutableListOf<Component>()

    base.components.forEach { baseComponent ->
      val branchComponent = branchComponents.remove(baseComponent.name)

      if (branchComponent == null) {
        removedComponents.add(baseComponent)
      } else {
        val componentSizeDiff = branchComponent.size - baseComponent.size

        if (componentSizeDiff.downloadSizeBytes != 0L && componentSizeDiff.installSizeBytes != 0L) {
          componentDiffs.add(
            ComponentDiff(
              componentSizeDiff,
              baseComponent,
              branchComponent
            )
          )
        }
      }
    }

    val addedComponents = branchComponents.values.toList()

    return AppSizeDiff(base, branch, sizeDiff, componentDiffs, addedComponents, removedComponents)
  }
}
