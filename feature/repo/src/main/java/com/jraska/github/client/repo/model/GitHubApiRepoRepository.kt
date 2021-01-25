package com.jraska.github.client.repo.model

import io.reactivex.Observable

internal class GitHubApiRepoRepository(
  private val gitHubRepoApi: RepoGitHubApi
) : RepoRepository {

  override fun getRepoDetail(owner: String, repoName: String): Observable<RepoDetail> {
    return gitHubRepoApi.getRepo(owner, repoName)
      .toObservable()
      .map { RepoConverter.convertToDetail(it) }
  }
}
