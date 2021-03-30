package com.jraska.github.client.repo.model

import org.threeten.bp.Instant

internal object RepoConverter {

  private fun convert(gitHubRepo: GitHubRepo): RepoHeader {
    return RepoHeader(
      gitHubRepo.owner!!.login!!,
      gitHubRepo.name!!, gitHubRepo.description ?: "",
      gitHubRepo.stargazersCount!!, gitHubRepo.forks!!
    )
  }

  fun convertToDetail(repo: GitHubRepo): RepoDetail {
    val header = convert(repo)

    val created = Instant.parse(repo.createdAt)
    val data = RepoDetail.Data(
      created, repo.openIssuesCount!!, repo.language!!,
      repo.subscribersCount!!
    )

    return RepoDetail(header, data, RepoDetail.PullRequestsState.Loading)
  }

  fun convertRepos(detail: RepoDetail, prs: List<GitHubPullRequest>): RepoDetail {
    val pulls = prs.map { convertPr(it) }
    return detail.copy(pullRequests = RepoDetail.PullRequestsState.PullRequests(pulls))
  }

  fun convertRepos(detail: RepoDetail, prsError: Throwable): RepoDetail {
    return detail.copy(pullRequests = RepoDetail.PullRequestsState.Error(prsError))
  }

  private fun convertPr(gitHubPr: GitHubPullRequest): RepoDetail.PullRequest {
    return RepoDetail.PullRequest(gitHubPr.title)
  }
}
