package com.jraska.github.client.release

interface GitHubApi {
  fun createMilestone(title: String): Int

  fun setMilestoneBody(milestoneNumber: Int, body: String)

  fun commentPr(prNumber: Int, body: String)

  fun assignMilestone(prNumber: Int, milestoneNumber: Int)

  fun setReleaseBody(release: String, body: String)

  fun listMergedPrsWithoutMilestone(): List<PullRequest>

  fun createRelease(version: String)

  fun prCommits(prNumber: Int): List<Commit>
}
