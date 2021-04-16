package com.jraska.github.client

import okhttp3.HttpUrl

class RecordingWebLinkLauncher : WebLinkLauncher {
  private val linksLaunched = mutableListOf<HttpUrl>()

  fun linksLaunched(): List<HttpUrl> = linksLaunched

  override fun launchOnWeb(url: HttpUrl) {
    linksLaunched.add(url)
  }
}

fun Fakes.recordingWebLinkLauncher() = RecordingWebLinkLauncher()
