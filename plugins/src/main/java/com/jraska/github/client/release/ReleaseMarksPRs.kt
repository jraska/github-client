package com.jraska.github.client.release

import com.jraska.analytics.AnalyticsReporter
import com.jraska.github.client.release.data.GitHubApiFactory
import com.jraska.github.client.release.data.LeadTimeReporter
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.time.Instant

object ReleaseMarksPRs {
  fun execute(tag: String) {
    val environment = Environment.create()
    val release = Release(tag, "https://github.com/jraska/github-client/releases/tag/$tag".toHttpUrl(), Instant.now())

    val api = GitHubApiFactory.create(environment)

    val leadTimeReporter = LeadTimeReporter(api, release, AnalyticsReporter.create("Lead Time Metrics"))
    val releaseMarker = ReleaseMarker(api, NotesComposer(), leadTimeReporter)

    releaseMarker.markPrsWithMilestone(release)
  }
}
