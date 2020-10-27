package com.jraska.github.client

interface Config {
  fun triggerRefresh()

  fun getBoolean(key: Key): Boolean

  fun getLong(key: Key): Long

  fun getString(key: Key): String

  class Key(val name: String, val owner: Owner)

  interface Decoration {
    fun decorate(config: Config): Config
  }
}
