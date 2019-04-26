package com.jraska.github.client.push

import com.jraska.github.client.Config
import javax.inject.Inject

internal class RefreshConfigCommand @Inject constructor(private val config: Config) : PushActionCommand {
  override fun execute(action: PushAction): Boolean {
    config.triggerRefresh()
    return true
  }
}
