package com.jraska.github.client.release

import okhttp3.HttpUrl

class Release(
  val releaseName: String,
  val releaseUrl: HttpUrl
)

class PullRequest(
  val number: Int,
  val title: String
)
