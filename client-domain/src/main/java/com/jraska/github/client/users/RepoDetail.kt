package com.jraska.github.client.users

import org.threeten.bp.LocalDateTime

class RepoDetail(val header: RepoHeader, val data: Data) {
  class Data(val created: LocalDateTime, val issuesCount: Int, val language: String, val subscribersCount: Int)
}
