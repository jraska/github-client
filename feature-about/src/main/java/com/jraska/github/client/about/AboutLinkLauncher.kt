package com.jraska.github.client.about

import android.app.Activity
import com.jraska.github.client.core.android.LinkLauncher
import okhttp3.HttpUrl

internal class AboutLinkLauncher : LinkLauncher {
  override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
    return if ("/about" == deepLink.encodedPath()) {
      AboutActivity.start(inActivity)
      LinkLauncher.Result.LAUNCHED
    } else {
      LinkLauncher.Result.NOT_LAUNCHED
    }
  }

  override fun priority(): LinkLauncher.Priority {
    return LinkLauncher.Priority.EXACT_MATCH
  }
}
