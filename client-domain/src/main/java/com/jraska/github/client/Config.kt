package com.jraska.github.client

interface Config {
  fun triggerRefresh()

  fun getBoolean(key: String): Boolean

  fun getLong(key: String): Long

  fun getString(key: String): String
}
