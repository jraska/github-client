package com.jraska.github.client.release

import com.jraska.github.client.release.data.GitHubApiFactory
import okhttp3.HttpUrl.Companion.toHttpUrl

object ReleaseMarksPRs {
  fun execute(tag: String) {
    val environment = Environment.create()
    val release = Release(tag, "${environment.baseUrl}releases/tag/$tag".toHttpUrl())

    val api = GitHubApiFactory.create(environment)
    val releaseMarker = ReleaseMarker(api, NotesComposer())

    releaseMarker.markPrsWithMilestone(release)
  }
}
