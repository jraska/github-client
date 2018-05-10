package com.jraska.github.client

import com.jraska.github.client.common.DeveloperError
import com.jraska.github.client.ui.BaseActivity
import com.jraska.github.client.ui.RepoDetailActivity
import com.jraska.github.client.ui.UserDetailActivity
import com.jraska.github.client.ui.UsersActivity
import okhttp3.HttpUrl
import timber.log.Timber
import javax.inject.Provider

internal class RealDeepLinkLauncher(private val topActivityProvider: Provider<BaseActivity>) : DeepLinkLauncher {
  override fun launch(deepLink: HttpUrl) {
    if (deepLink.host() != "github.com") {
      throw IllegalArgumentException("We execute only GitHub deep links, not: " + deepLink)
    }

    Timber.i("Launching %s", deepLink)

    if ("/users" == deepLink.encodedPath()) {
      UsersActivity.start(topActivityProvider.get())
      return
    }

    if (deepLink.pathSize() == 2) {
      val fullRepoPath = deepLink.pathSegments()[0] + "/" + deepLink.pathSegments()[1]
      RepoDetailActivity.start(topActivityProvider.get(), fullRepoPath)
      return
    }

    if (deepLink.pathSize() == 1) {
      val login = deepLink.pathSegments()[0]
      UserDetailActivity.start(topActivityProvider.get(), login)
      return
    }

    throw IllegalArgumentException("Unexpected deep link: " + deepLink)
  }
}
