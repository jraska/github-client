package com.jraska.github.client.settings

import android.app.Activity
import com.jraska.github.client.core.android.LinkLauncher
import okhttp3.HttpUrl
import javax.inject.Inject

internal class SettingsLinkLauncher @Inject constructor() : LinkLauncher {
  override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
    return if ("/settings" == deepLink.encodedPath) {
      SettingsActivity.start(inActivity)
      LinkLauncher.Result.LAUNCHED
    } else {
      LinkLauncher.Result.NOT_LAUNCHED
    }
  }

  override fun priority(): LinkLauncher.Priority = LinkLauncher.Priority.EXACT_MATCH
}
