package com.jraska.github.client.config

import com.jraska.github.client.Config

class MutableConfigDef(
  val key: Config.Key,
  val type: MutableConfigType,
  val domain: List<Any?>
)

enum class MutableConfigType {
  BOOLEAN,
  LONG,
  STRING
}
