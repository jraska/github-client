package com.jraska.github.client.push

import com.jraska.github.client.Config

class RefreshConfigCommand constructor(private val config: Config) : PushActionCommand {
  override fun execute(action: PushAction): Boolean {
    config.triggerRefresh()
    return true
  }
}
