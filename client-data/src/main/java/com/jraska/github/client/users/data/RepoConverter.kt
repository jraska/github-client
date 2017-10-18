package com.jraska.github.client.users.data

import com.jraska.github.client.users.RepoDetail
import com.jraska.github.client.users.RepoHeader

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

internal class RepoConverter {

  fun convert(gitHubRepo: GitHubRepo): RepoHeader {
    return RepoHeader(gitHubRepo.owner!!.login!!, gitHubRepo.name!!, gitHubRepo.description!!,
      gitHubRepo.stargazersCount!!, gitHubRepo.forks!!)
  }

  fun convertToDetail(repo: GitHubRepo): RepoDetail {
    val header = convert(repo)

    val created = LocalDateTime.parse(repo.createdAt, DateTimeFormatter.ISO_DATE_TIME)
    val data = RepoDetail.Data(created, repo.openIssuesCount!!, repo.language!!,
      repo.subscribersCount!!)

    return RepoDetail(header, data)
  }

  companion object {
    val INSTANCE = RepoConverter()
  }
}
