package com.jraska.devanalytics.github.model

const val MIXPANEL_KEY_ENV_VAR = "MIXPANEL_API_KEY"

class Environment(
  val mixpanelUrl: String?,
  val mixpanelToken: String
) {
  companion object {
    fun create(): Environment {
      val mixpanelKey = System.getenv(MIXPANEL_KEY_ENV_VAR)
      return Environment(null, mixpanelKey)
    }
  }
}
