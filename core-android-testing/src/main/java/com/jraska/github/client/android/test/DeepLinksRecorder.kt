package com.jraska.github.client.android.test

import android.app.Activity
import com.jraska.github.client.core.android.LinkLauncher
import okhttp3.HttpUrl

class DeepLinksRecorder : LinkLauncher {
  val linksLaunched = mutableListOf<HttpUrl>()
  private var swallowLinks = false

  override fun priority() = LinkLauncher.Priority.TESTING

  override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
    linksLaunched.add(deepLink)

    return if (swallowLinks) {
        LinkLauncher.Result.LAUNCHED // swallows link only as recorded
    } else {
        LinkLauncher.Result.NOT_LAUNCHED
    }
  }

  fun usingLinkRecording(block: (DeepLinksRecorder) -> Unit) {
    try {
      swallowLinks = true
      block(this)
    } finally {
      swallowLinks = false
      linksLaunched.clear()
    }
  }
}
