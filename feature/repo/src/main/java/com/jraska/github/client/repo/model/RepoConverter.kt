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

    return RepoDetail(header, data)
  }
}
