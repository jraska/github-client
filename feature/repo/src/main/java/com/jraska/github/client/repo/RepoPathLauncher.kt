package com.jraska.github.client.repo

import android.app.Activity
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.repo.ui.RepoDetailActivity
import okhttp3.HttpUrl

internal class RepoPathLauncher : LinkLauncher {
  override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
    if (deepLink.pathSize == 2) {
      val fullRepoPath = deepLink.pathSegments[0] + "/" + deepLink.pathSegments[1]
      RepoDetailActivity.start(inActivity, fullRepoPath)
      return LinkLauncher.Result.LAUNCHED
    }

    return LinkLauncher.Result.NOT_LAUNCHED
  }

  override fun priority(): LinkLauncher.Priority {
    return LinkLauncher.Priority.PATH_LENGTH
  }
}
