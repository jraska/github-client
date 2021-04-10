package com.jraska.github.client.release.data

import com.jraska.analytics.AnalyticsEvent
import com.jraska.analytics.AnalyticsReporter
import com.jraska.github.client.release.Commit
import com.jraska.github.client.release.GitHubApi
import com.jraska.github.client.release.PullRequest
import com.jraska.github.client.release.Release
import java.time.Duration

class LeadTimeReporter(
  private val api: GitHubApi,
  private val release: Release,
  private val reporter: AnalyticsReporter
) {
  fun reportLeadTime(pulls: List<PullRequest>) {
    val events = pulls.flatMap { api.prCommits(it.number) }
      .map { toEvent(it) }

    reporter.report(*events.toTypedArray())
  }

  private fun toEvent(commit: Commit): AnalyticsEvent {
    return AnalyticsEvent(
      "Commit Released",
      mapOf(
        "leadTimeSec" to Duration.between(commit.time, release.timestamp).seconds,
        "gitCommit" to commit.sha,
        "author" to commit.author,
        "prNumber" to commit.prNumber,
        "releaseName" to release.releaseName
      )
    )
  }
}
