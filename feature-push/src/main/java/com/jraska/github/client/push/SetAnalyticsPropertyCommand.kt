package com.jraska.github.client.push

import com.jraska.github.client.common.BooleanResult
import com.jraska.github.client.common.BooleanResult.FAILURE
import com.jraska.github.client.common.BooleanResult.SUCCESS
import com.jraska.github.client.analytics.AnalyticsProperty
import javax.inject.Inject

internal class SetAnalyticsPropertyCommand @Inject constructor(
  private val analyticsProperty: AnalyticsProperty
) : PushActionCommand {
  override fun execute(action: PushAction): BooleanResult {
    val key = action.parameters["property_key"] ?: return FAILURE
    val value = action.parameters["property_value"] ?: return FAILURE

    analyticsProperty.setUserProperty(key, value)
    return SUCCESS
  }
}
