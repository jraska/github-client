package com.jraska.module

data class ProjectStatistics(
  val modulesCount: Int,
  val apiModules: Int,
  val appModules: Int,
  val implementationModules: Int,
  val prodKotlinTotalLines: Int,
  val prodJavaTotalLines: Int,
  val prodXmlTotalLines: Int,
  val moduleStatistics: List<ModuleStatistics>,
  val externalDependencies: List<ModuleArtifactDependency>
)

data class ModuleStatistics(
  val moduleName: String,
  val containedProdFiles: Collection<FileTypeStatistics>,
  val containedUnitTestFiles: Collection<FileTypeStatistics>,
  val containedAndroidTestFiles: Collection<FileTypeStatistics>,
  val type: ModuleType
)

data class FileTypeStatistics(
  val lineCount: Int,
  val fileCount: Int,
  val type: FileType
)

data class ModuleArtifactDependency(
  val moduleName: String,
  val type: ModuleType,
  val fullName: String,
  val group: String,
  val artifact: String,
  val version: String?,
  val configurationName: String
)

enum class ModuleType {
  Api,
  Implementation,
  App
}

enum class FileType(val suffix: String) {
  KOTLIN(".kt"),
  JAVA(".java"),
  XML(".xml")
}

