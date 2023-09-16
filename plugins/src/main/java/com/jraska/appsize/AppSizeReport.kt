package com.jraska.appsize

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
)

enum class ComponentType {
  INTERNAL,
  EXTERNAL
}

class Component(
  val name: String,
  val type: ComponentType,
  val size: AppSize
)
