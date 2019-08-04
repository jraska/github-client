package com.jraska.github.client.about.entrance

import android.app.Activity
import android.content.Intent
import com.jraska.github.client.core.android.LinkLauncher
import okhttp3.HttpUrl

class DynamicAboutLinkLauncher : LinkLauncher {
  override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
    return if ("/about" == deepLink.encodedPath) {
      installAndLaunchAboutFeature(inActivity)
      LinkLauncher.Result.LAUNCHED
    } else {
      LinkLauncher.Result.NOT_LAUNCHED
    }
  }

  override fun priority(): LinkLauncher.Priority = LinkLauncher.Priority.EXACT_MATCH

  private fun installAndLaunchAboutFeature(inActivity: Activity) {
    inActivity.startActivity(Intent(inActivity, AboutLaunchActivity::class.java))
  }
}
