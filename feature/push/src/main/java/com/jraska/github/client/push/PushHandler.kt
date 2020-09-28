package com.jraska.github.client.push

import com.jraska.github.client.common.BooleanResult
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class PushHandler @Inject internal constructor(
  private val pushCommands: Map<String, @JvmSuppressWildcards Provider<PushActionCommand>>
) {

  internal fun handlePush(action: PushAction): BooleanResult {
    Timber.d("Push received action: %s", action.name)

    val result = handleInternal(action)

    Timber.d("Push result: %s, Action: %s", result, action)

    return result
  }

  private fun handleInternal(action: PushAction): BooleanResult {
    val actionCommand = pushCommands[action.name] ?: return BooleanResult.FAILURE

    return actionCommand.get().execute(action)
  }
}
