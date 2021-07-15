package com.jraska.github.client.release.data

import com.jraska.github.client.release.Commit
import com.jraska.github.client.release.GitHubApi
import com.jraska.github.client.release.PullRequest
import java.time.Instant

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

  override fun listMergedPrsWithoutMilestone(): List<PullRequest> {
    val pulls = mutableListOf<PullRequest>()

    var page = 1
    do {
      val previousSize = pulls.size

      api.getPulls(page)
        .execute()
        .body()!!
        .filter { it.milestone == null }
        .filter { it.mergedAt != null }
        .map { PullRequest(it.number, it.title) }
        .also { pulls.addAll(it) }
      page++
    } while (previousSize != pulls.size)

    return pulls
  }

  override fun createRelease(version: String) {
    api.createRelease(CreateReleaseDto(version)).execute()
  }

  override fun prCommits(prNumber: Int): List<Commit> {
    return api.commits(prNumber).execute().body()!!.map {
      Commit(
        it.sha,
        Instant.parse(it.commit.author.dateString),
        it.author.login,
        it.commit.message,
        prNumber
      )
    }
  }
}
