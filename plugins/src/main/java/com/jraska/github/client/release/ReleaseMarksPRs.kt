package com.jraska.github.client.release

import com.jraska.github.client.release.data.GitHubApiFactory
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.time.Instant

object ReleaseMarksPRs {
  fun execute(tag: String) {
    val environment = Environment.create()
    val release = Release(tag, "https://github.com/jraska/github-client/releases/tag/$tag".toHttpUrl(), Instant.now())

    val api = GitHubApiFactory.create(environment)
    val releaseMarker = ReleaseMarker(api, NotesComposer())

    releaseMarker.markPrsWithMilestone(release)
  }
}
