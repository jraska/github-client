package com.jraska.github.client.push

import com.jraska.github.client.Config
import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.common.BooleanResult
import com.jraska.github.client.common.BooleanResult.FAILURE
import javax.inject.Inject

internal class ConfigAsPropertyCommand @Inject constructor(
  private val config: Config,
  private val analyticsProperty: AnalyticsProperty
) : PushActionCommand {
  override fun execute(action: PushAction): BooleanResult {
    val key = action.parameters["config_key"] ?: return FAILURE

    val value = config.getString(key)
    analyticsProperty.setUserProperty(key, value)
    return BooleanResult.SUCCESS
  }
}
