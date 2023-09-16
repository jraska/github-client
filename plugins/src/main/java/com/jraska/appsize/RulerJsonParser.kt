package com.jraska.appsize

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.File

object RulerJsonParser {

  fun parse(jsonFile: File): AppSizeReport {
    val rulerReportDto = Gson().fromJson(jsonFile.bufferedReader(), RulerReportDto::class.java)

    return AppSizeReport(
      name = rulerReportDto.name,
      AppSize(rulerReportDto.downloadSize, rulerReportDto.installSize),
      version = rulerReportDto.version,
      variant = rulerReportDto.variant,
      components = rulerReportDto.components.map {
        Component(it.name, ComponentType.valueOf(it.type), AppSize(it.downloadSize, it.installSize))
      }
    )
  }
}

class RulerReportDto(
  @SerializedName("name")
  val name: String,

  @SerializedName("downloadSize")
  val downloadSize: Long,

  @SerializedName("version")
  val version: String,

  @SerializedName("installSize")
  val installSize: Long,

  @SerializedName("variant")
  val variant: String,

  @SerializedName("components")
  val components: List<ComponentDto>
)

class ComponentDto(
  @SerializedName("downloadSize")
  val downloadSize: Long,

  @SerializedName("name")
  val name: String,

  @SerializedName("type")
  val type: String,

  @SerializedName("installSize")
  val installSize: Long,
)

