package com.jraska.appsize

import java.math.RoundingMode
import java.text.DecimalFormat

class AppSizeReport(
  val name: String,
  val size: AppSize,
  val version: String,
  val variant: String,
  val components: List<Component>
)

class AppSize(
  val downloadSizeBytes: Long,
  val installSizeBytes: Long
) {
  operator fun minus(other: AppSize): AppSize {
    return AppSize(
      downloadSizeBytes - other.downloadSizeBytes,
      installSizeBytes - other.installSizeBytes
    )
  }

  override fun toString(): String {
    return "Download: ${sizeString(downloadSizeBytes)}, Install: ${sizeString(installSizeBytes)}"
  }

  private fun sizeString(size: Long): String {


    if (size < 1_000) {
      return "$size B"
    } else if (size < 1_000_000) {
      val kBytes = size / 1000.0
      val sizeText = FORMATTER.format(kBytes)

      return "$sizeText kB"
    } else {
      val mBytes = size / 1_000_000.0
      val sizeText = FORMATTER.format(mBytes)

      return "$sizeText MB"
    }
  }

  companion object {
    private val FORMATTER = DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }
  }
}

enum class ComponentType {
  INTERNAL,
  EXTERNAL
}

class Component(
  val name: String,
  val type: ComponentType,
  val size: AppSize
)
