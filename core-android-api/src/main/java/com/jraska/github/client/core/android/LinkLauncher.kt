package com.jraska.github.client.core.android

import android.app.Activity
import okhttp3.HttpUrl

interface LinkLauncher {
  fun launch(inActivity: Activity, deepLink: HttpUrl): Result

  fun priority(): Priority

  enum class Result {
    LAUNCHED,
    NOT_LAUNCHED
  }

  enum class Priority(val value: Int) {
    TESTING(0),
    EXACT_MATCH(1),
    PATH_LENGTH(2)
  }
}
