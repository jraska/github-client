package com.jraska.github.client.users.data

import com.jraska.github.client.users.RepoDetail
import com.jraska.github.client.users.RepoHeader
import org.threeten.bp.Instant

internal class RepoConverter {

  fun convert(gitHubRepo: GitHubRepo): RepoHeader {
    return RepoHeader(gitHubRepo.owner!!.login!!,
      gitHubRepo.name!!, gitHubRepo.description ?: "",
      gitHubRepo.stargazersCount!!, gitHubRepo.forks!!)
  }

  fun convertToDetail(repo: GitHubRepo): RepoDetail {
    val header = convert(repo)

    val created = Instant.parse(repo.createdAt)
    val data = RepoDetail.Data(created, repo.openIssuesCount!!, repo.language!!,
      repo.subscribersCount!!)

    return RepoDetail(header, data)
  }

  companion object {
    val INSTANCE = RepoConverter()
  }
}
