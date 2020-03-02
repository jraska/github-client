package com.jraska.github.client.core.android

import android.app.Activity
import com.jraska.github.client.DeepLinkLauncher
import okhttp3.HttpUrl
import timber.log.Timber

class RealDeepLinkLauncher private constructor(
  private val topActivityProvider: TopActivityProvider,
  private val launchers: List<LinkLauncher>
) : DeepLinkLauncher {
  override fun launch(deepLink: HttpUrl) {
    if (deepLink.host != "github.com") {
      throw IllegalArgumentException("We handle only GitHub deep links, not: $deepLink")
    }

    Timber.i("Launching %s", deepLink)

    topActivityProvider.onTopActivity {
      launchInActivity(it, deepLink)
    }
  }

  private fun launchInActivity(activity: Activity, deepLink: HttpUrl) {
    for (launcher in launchers) {
      val result = launcher.launch(activity, deepLink)
      if (result == LinkLauncher.Result.LAUNCHED) {
        return
      }
    }

    throw IllegalArgumentException("Unexpected deep link: $deepLink")
  }

  companion object {
    fun create(activityProvider: TopActivityProvider, launchers: Set<LinkLauncher>): RealDeepLinkLauncher {
      val sortedLaunchers = launchers.sortedBy { it.priority().value }
      return RealDeepLinkLauncher(activityProvider, sortedLaunchers)
    }
  }
}
