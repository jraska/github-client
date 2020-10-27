package com.jraska.github.client.config.debug

class MutableConfigDef(
  val name: String,
  val type: MutableConfigType,
  val domain: List<Any?>
)

enum class MutableConfigType {
  BOOLEAN,
  LONG,
  STRING
}
