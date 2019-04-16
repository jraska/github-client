package com.jraska.github.client.users.model

import org.threeten.bp.Instant

internal class RepoDetail(val header: RepoHeader, val data: Data) {
  class Data(val created: Instant, val issuesCount: Int, val language: String, val subscribersCount: Int)
}
