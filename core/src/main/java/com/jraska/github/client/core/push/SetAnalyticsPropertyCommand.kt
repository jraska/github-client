package com.jraska.github.client.core.push

import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.push.PushAction
import com.jraska.github.client.push.PushActionCommand
import com.jraska.github.client.push.PushExecuteResult
import javax.inject.Inject

internal class SetAnalyticsPropertyCommand @Inject constructor(
  private val analyticsProperty: AnalyticsProperty
) : PushActionCommand {
  override fun execute(action: PushAction): PushExecuteResult {
    val key = action.parameters["property_key"] ?: return PushExecuteResult.FAILURE
    val value = action.parameters["property_value"] ?: return PushExecuteResult.FAILURE

    analyticsProperty.setUserProperty(key, value)
    return PushExecuteResult.SUCCESS
  }
}
