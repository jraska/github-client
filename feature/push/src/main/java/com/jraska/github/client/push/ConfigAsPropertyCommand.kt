package com.jraska.github.client.push

import com.jraska.github.client.Config
import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsProperty
import javax.inject.Inject

internal class ConfigAsPropertyCommand @Inject constructor(
  private val config: Config,
  private val analyticsProperty: AnalyticsProperty
) : PushActionCommand {
  override fun execute(action: PushAction): PushExecuteResult {
    val key = action.parameters["config_key"] ?: return PushExecuteResult.FAILURE

    val value = config.getString(Config.Key(key, Owner.UNKNOWN_STALE))
    analyticsProperty.setUserProperty(key, value)
    return PushExecuteResult.SUCCESS
  }
}
