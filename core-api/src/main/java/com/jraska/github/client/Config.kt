package com.jraska.github.client

interface Config {
  fun getBoolean(key: Key): Boolean

  fun getLong(key: Key): Long

  fun getString(key: Key): String

  fun triggerRefresh()

  class Key(val name: String, val owner: Owner)

  interface Decoration {
    fun decorate(originalConfig: Config): Config
  }
}
