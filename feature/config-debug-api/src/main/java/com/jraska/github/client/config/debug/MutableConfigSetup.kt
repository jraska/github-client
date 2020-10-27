package com.jraska.github.client.config.debug

/**
 * Provide this into Set to module to appear in settings options
 */
interface MutableConfigSetup {
  fun mutableConfigs(): List<MutableConfigDef>
}
