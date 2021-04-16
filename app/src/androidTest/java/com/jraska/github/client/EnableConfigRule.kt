package com.jraska.github.client

import org.junit.rules.ExternalResource

class EnableConfigRule(
  private val key: String,
  private val value: Any
) : ExternalResource() {

  lateinit var revert: FakeConfig.RevertSet

  override fun before() {
    revert = TestUITestApp.get().appComponent.config.set(key, value)
  }

  override fun after() {
    revert.revert()
  }
}
