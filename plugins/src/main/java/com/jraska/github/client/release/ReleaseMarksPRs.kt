package com.jraska.github.client.release

import com.jraska.github.client.release.data.GitHubApiFactory
import okhttp3.HttpUrl.Companion.toHttpUrl

object ReleaseMarksPRs {
  fun execute(tag: String) {
    val release = Release(tag, "https://github.com/jraska/github-client/releases/tag/$tag".toHttpUrl())

    val api = GitHubApiFactory.create()
    val releaseMarker = ReleaseMarker(api, NotesComposer())

    releaseMarker.markPrsWithMilestone(release)
  }
}
