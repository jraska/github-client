package com.jraska.github.client.about.entrance.internal

import android.app.Activity
import android.content.Intent
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.core.android.TopActivityProvider
import com.jraska.github.client.dynamicbase.DynamicFeatureInstaller
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.settings.entrance.R
import okhttp3.HttpUrl
import timber.log.Timber
import javax.inject.Inject

internal class DynamicSettingsLinkLauncher @Inject constructor(
  val installer: DynamicFeatureInstaller,
  val appSchedulers: AppSchedulers,
  val topActivityProvider: TopActivityProvider
) : LinkLauncher {
  override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
    return if ("/settings" == deepLink.encodedPath) {
      installAndLaunchSettingsFeature(inActivity)
      LinkLauncher.Result.LAUNCHED
    } else {
      LinkLauncher.Result.NOT_LAUNCHED
    }
  }

  override fun priority(): LinkLauncher.Priority = LinkLauncher.Priority.EXACT_MATCH

  private fun installAndLaunchSettingsFeature(inActivity: Activity) {
    val aboutFeature = inActivity.getString(R.string.title_dynamic_feature_settings)

    installer.ensureInstalled(aboutFeature)
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.mainThread)
      .subscribe({
        val activity = topActivityProvider.get()
        activity.startActivity(launchIntent(activity))
      }, { Timber.e(it) })
  }

  private fun launchIntent(inActivity: Activity): Intent {
    return Intent().apply {
      setClassName(inActivity.packageName, "com.jraska.github.client.settings.SettingsActivity")
    }
  }
}
