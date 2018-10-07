package com.jraska.github.client.push

import com.jraska.github.client.Config
import com.jraska.github.client.analytics.AnalyticsProperty

class ConfigAsPropertyCommand constructor(
  private val config: Config,
  private val analyticsProperty: AnalyticsProperty
) : PushActionCommand {
  override fun execute(action: PushAction): Boolean {
    val key = action.parameters["config_key"] ?: return false

    val value = config.getString(key)
    analyticsProperty.setUserProperty(key, value)
    return true
  }
}
