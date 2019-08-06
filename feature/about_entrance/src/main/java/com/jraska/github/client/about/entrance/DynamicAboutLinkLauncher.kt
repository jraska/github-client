package com.jraska.github.client.about.entrance

import android.app.Activity
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
    DynamicFeatureActivity.start(inActivity, inActivity.getString(R.string.title_dynamic_feature_about))
  }
}
