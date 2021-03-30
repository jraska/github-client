package com.jraska.github.client.repo.model

import org.threeten.bp.Instant

internal data class RepoDetail(
  val header: RepoHeader,
  val data: Data,
  val pullRequests: PullRequestsState
) {
  class Data(val created: Instant, val issuesCount: Int, val language: String, val subscribersCount: Int)

  sealed class PullRequestsState {
    object Loading : PullRequestsState()
    class PullRequests(val pulls: List<PullRequest>) : PullRequestsState()
    class Error(error: Throwable) : PullRequestsState()
  }

  class PullRequest(val title: String)
}
