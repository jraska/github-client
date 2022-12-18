package com.jraska.github.client.push

interface PushActionCommand {
  fun execute(action: PushAction): PushExecuteResult
}

enum class PushExecuteResult {
  SUCCESS,
  FAILURE
}
