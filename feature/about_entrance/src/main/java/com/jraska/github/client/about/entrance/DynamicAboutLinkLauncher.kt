package com.jraska.github.client.about.entrance

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.core.android.TopActivityProvider
import com.jraska.github.client.rx.AppSchedulers
import okhttp3.HttpUrl
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DynamicAboutLinkLauncher @Inject constructor(
  val installer: DynamicFeatureInstaller,
  val appSchedulers: AppSchedulers,
  val topActivityProvider: TopActivityProvider
) : LinkLauncher {
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
    val aboutFeature = inActivity.getString(R.string.title_dynamic_feature_about)

    installer.ensureInstalled(aboutFeature)
      .subscribeOn(appSchedulers.io)
      .observeOn(appSchedulers.mainThread)
      .subscribe({
        val activity = topActivityProvider.get()

        try{
          activity.startActivity(launchIntent(activity))
        } catch (x: Exception) {
          Timber.e(x)
          Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
        }
      }, { Timber.e(it) })
  }

  private fun launchIntent(inActivity: Activity): Intent {
    return Intent().apply {
      setClassName(inActivity.packageName, "com.jraska.github.client.about.AboutActivity")
    }
  }
}
