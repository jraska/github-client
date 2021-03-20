package com.jraska.github.client.release

class ReleaseMarker(
  private val gitHubApi: GitHubApi,
  private val notesComposer: NotesComposer
) {
  fun markPrsWithMilestone(release: Release) {
    val pullRequests = gitHubApi.listPrsWithoutMilestone()

    val milestoneNumber = gitHubApi.createMilestone(release.releaseName)

    pullRequests.forEach { pr ->
      gitHubApi.assignMilestone(pr.number, milestoneNumber)

      val commentBody = notesComposer.prReleaseComment(release)
      gitHubApi.commentPr(pr.number, commentBody)
    }

    val releaseNotes = notesComposer.releaseNotes(pullRequests)
    gitHubApi.setMilestoneBody(milestoneNumber, releaseNotes)
    gitHubApi.setReleaseBody(release.releaseName, releaseNotes)
  }
}
