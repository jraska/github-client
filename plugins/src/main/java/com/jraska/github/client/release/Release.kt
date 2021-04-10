package com.jraska.github.client.release

import okhttp3.HttpUrl
import java.time.Instant

class Release(
  val releaseName: String,
  val releaseUrl: HttpUrl
)

class PullRequest(
  val number: Int,
  val title: String
)

class Commit(
  val sha: String,
  val time: Instant,
  val author: String,
  val prNumber: Int
)
