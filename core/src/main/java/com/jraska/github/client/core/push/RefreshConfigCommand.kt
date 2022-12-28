package com.jraska.github.client.core.push

import com.jraska.github.client.Config
import com.jraska.github.client.push.PushAction
import com.jraska.github.client.push.PushActionCommand
import com.jraska.github.client.push.PushExecuteResult
import javax.inject.Inject

internal class RefreshConfigCommand @Inject constructor(private val config: Config) :
  PushActionCommand {
  override fun execute(action: PushAction): PushExecuteResult {
    config.triggerRefresh()
    return PushExecuteResult.SUCCESS
  }
}
