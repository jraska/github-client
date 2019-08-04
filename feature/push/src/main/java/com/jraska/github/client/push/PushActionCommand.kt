package com.jraska.github.client.push

import com.jraska.github.client.common.BooleanResult

interface PushActionCommand {
  fun execute(action: PushAction): BooleanResult
}
