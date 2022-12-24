package com.jraska.github.client

import org.junit.rules.ExternalResource

class EnableConfigRule(
  private val entry: Pair<String, Any>? = null
) : ExternalResource() {

  private val reverts = mutableSetOf<FakeConfig.RevertSet>()
  private val config get() = TestUITestApp.get().appComponent.config

  fun set(entry: Pair<String, Any>) {
    reverts.add(config.set(entry.first, entry.second))
  }

  override fun before() {
    if (entry != null) {
      set(entry)
    }
  }

  override fun after() {
    reverts.forEach { it.revert() }
  }
}
