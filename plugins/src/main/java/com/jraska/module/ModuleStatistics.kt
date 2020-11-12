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

