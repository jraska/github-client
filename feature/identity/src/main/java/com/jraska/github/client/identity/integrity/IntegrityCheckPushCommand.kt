package com.jraska.github.client.identity.integrity

import com.jraska.github.client.identity.IntegrityTrigger
import com.jraska.github.client.push.PushAction
import com.jraska.github.client.push.PushActionCommand
import com.jraska.github.client.push.PushExecuteResult
import javax.inject.Inject

class IntegrityCheckPushCommand @Inject constructor(
  private val integrityTrigger: IntegrityTrigger
) : PushActionCommand {
  override fun execute(action: PushAction): PushExecuteResult {
    integrityTrigger.executeIntegrityCheck()

    return PushExecuteResult.SUCCESS
  }
}
