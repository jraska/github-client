package com.jraska.github.client

import android.app.Activity
import com.jraska.github.client.ui.RepoDetailActivity
import com.jraska.github.client.ui.UserDetailActivity
import okhttp3.HttpUrl

class UsersPathLauncher : LinkLauncher {
  override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
    if (deepLink.pathSize() == 2) {
      val fullRepoPath = deepLink.pathSegments()[0] + "/" + deepLink.pathSegments()[1]
      RepoDetailActivity.start(inActivity, fullRepoPath)
      return LinkLauncher.Result.LAUNCHED
    }

    if (deepLink.pathSize() == 1) {
      val login = deepLink.pathSegments()[0]
      UserDetailActivity.start(inActivity, login)
      return LinkLauncher.Result.LAUNCHED
    }

    return LinkLauncher.Result.NOT_LAUNCHED
  }

  override fun priority(): LinkLauncher.Priority {
    return LinkLauncher.Priority.PATH_LENGTH
  }
}
