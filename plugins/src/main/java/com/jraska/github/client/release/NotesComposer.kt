package com.jraska.github.client.release

class NotesComposer {
  fun releaseNotes(pullRequests: List<PullRequest>): String {
    return pullRequests.joinToString(separator = "  \r\n", transform = { pr -> "#${pr.number}: ${pr.title}" })
  }

  fun prReleaseComment(release: Release): String {
    return "This PR was released with [${release.releaseName}](${release.releaseUrl})"
  }
}
