package com.jraska.github.client.core.android

import android.app.Activity
import com.jraska.github.client.DeepLinkLauncher
import okhttp3.HttpUrl
import timber.log.Timber
import javax.inject.Provider

class RealDeepLinkLauncher private constructor(
  private val topActivityProvider: Provider<Activity>,
  private val launchers: List<LinkLauncher>
) : DeepLinkLauncher {
  override fun launch(deepLink: HttpUrl) {
    if (deepLink.host() != "github.com") {
      throw IllegalArgumentException("We handle only GitHub deep links, not: $deepLink")
    }

    Timber.i("Launching %s", deepLink)

    val activity = topActivityProvider.get()
    for (launcher in launchers) {
      val result = launcher.launch(activity, deepLink)
      if (result == LinkLauncher.Result.LAUNCHED) {
        return
      }
    }

    throw IllegalArgumentException("Unexpected deep link: $deepLink")
  }

  companion object {
    fun create(activityProvider: Provider<Activity>, launchers: Set<LinkLauncher>): RealDeepLinkLauncher {
      val sortedLaunchers = launchers.sortedBy { it.priority().value }
      return RealDeepLinkLauncher(activityProvider, sortedLaunchers)
    }
  }
}
