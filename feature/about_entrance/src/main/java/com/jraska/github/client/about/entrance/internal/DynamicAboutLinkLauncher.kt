package com.jraska.github.client.about.entrance.internal

import android.app.Activity
import android.content.Intent
import com.jraska.github.client.about.entrance.R
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.core.android.TopActivityProvider
import com.jraska.github.client.dynamicbase.DynamicFeatureInstaller
import com.jraska.github.client.rx.AppSchedulers
import okhttp3.HttpUrl
import timber.log.Timber
import javax.inject.Inject

internal class DynamicAboutLinkLauncher @Inject constructor(
  val installer: DynamicFeatureInstaller,
  val appSchedulers: AppSchedulers,
  val topActivityProvider: TopActivityProvider
) : LinkLauncher {
  override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
    return if ("/about" == deepLink.encodedPath) {
      installAndLaunchAboutFeature(inActivity.getString(R.string.title_dynamic_feature_about))
      LinkLauncher.Result.LAUNCHED
    } else {
      LinkLauncher.Result.NOT_LAUNCHED
    }
  }

  override fun priority(): LinkLauncher.Priority = LinkLauncher.Priority.EXACT_MATCH

  private fun installAndLaunchAboutFeature(featureName: String) {
    installer.ensureInstalled(featureName)
      .andThen(topActivityProvider.topActivity())
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.mainThread)
      .subscribe({
        it.startActivity(launchIntent(it))
      }, { Timber.e(it) })
  }

  private fun launchIntent(inActivity: Activity): Intent {
    return Intent().apply {
      setClassName(inActivity.packageName, "com.jraska.github.client.about.AboutActivity")
    }
  }
}
