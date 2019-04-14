package com.jraska.github.client.push

interface PushActionCommand {
  fun execute(action: PushAction): Boolean
}
