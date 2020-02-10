package com.jraska.github.client.push

import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.common.BooleanResult
import javax.inject.Inject
import javax.inject.Provider
import timber.log.Timber

class PushHandler @Inject internal constructor(
  private val eventAnalytics: EventAnalytics,
  private val pushCommands: Map<String, @JvmSuppressWildcards Provider<PushActionCommand>>
) {

  internal fun handlePush(action: PushAction): BooleanResult {
    Timber.v("Push received action: %s", action.name)

    return handleInternal(action)
  }

  private fun handleInternal(action: PushAction): BooleanResult {
    val actionCommand = pushCommands[action.name] ?: return BooleanResult.FAILURE

    return actionCommand.get().execute(action)
  }
}
