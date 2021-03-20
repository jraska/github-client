package com.jraska.github.client.release.data

import com.jraska.github.client.release.GitHubApi
import com.jraska.github.client.release.PullRequest

class GitHubApiImpl(
  private val api: RetrofitGitHubApi
) : GitHubApi {
  override fun createMilestone(title: String): Int {
    return api.createMilestone(MilestoneDto(title)).execute().body()!!.number
  }

  override fun setMilestoneBody(milestoneNumber: Int, body: String) {
    api.updateMilestone(milestoneNumber, UpdateMilestoneDto(body)).execute()
  }

  override fun commentPr(prNumber: Int, body: String) {
    api.sendComment(prNumber, CommentRequestDto(body)).execute()
  }

  override fun assignMilestone(prNumber: Int, milestoneNumber: Int) {
    api.assignMilestone(prNumber, AssignMilestoneDto(milestoneNumber)).execute()
  }

  override fun setReleaseBody(release: String, body: String) {
    val releaseId = api.getRelease(release).execute().body()!!.id
    println(releaseId)

    api.setReleseBody(releaseId, ReleaseBodyDto(body)).execute()
  }

  override fun listPrsWithoutMilestone(): List<PullRequest> {
    val pulls = mutableListOf<PullRequest>()

    var page = 1
    do {
      val previousSize = pulls.size

      api.getPulls(page)
        .execute()
        .body()!!
        .filter { it.milestone == null }
        .map { PullRequest(it.number, it.title) }
        .also { pulls.addAll(it) }
      page++
    } while (previousSize != pulls.size)

    return pulls
  }
}
