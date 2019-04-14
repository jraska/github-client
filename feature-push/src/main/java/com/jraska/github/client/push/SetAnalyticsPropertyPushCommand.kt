package com.jraska.github.client.push

import com.jraska.github.client.analytics.AnalyticsProperty

class SetAnalyticsPropertyPushCommand constructor(private val analyticsProperty: AnalyticsProperty) : PushActionCommand {
  override fun execute(action: PushAction): Boolean {
    val key = action.parameters["property_key"] ?: return false
    val value = action.parameters["property_value"] ?: return false

    analyticsProperty.setUserProperty(key, value)
    return true
  }
}
