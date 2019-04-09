package com.jraska.github.client

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
    EXACT_MATCH(0),
    PATH_LENGTH(1)
  }
}
