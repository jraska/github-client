package com.jraska.github.client.config

/**
 * Provide this into Set to module to appear in settings options
 */
interface MutableConfigSetup {
  fun mutableConfigs(): List<MutableConfigDef>
}
