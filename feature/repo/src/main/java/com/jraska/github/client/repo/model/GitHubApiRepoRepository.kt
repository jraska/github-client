package com.jraska.github.client.repo.model

import io.reactivex.Observable

internal class GitHubApiRepoRepository(
  private val gitHubRepoApi: RepoGitHubApi
) : RepoRepository {

  override fun getRepoDetail(owner: String, repoName: String): Observable<RepoDetail> {
    return gitHubRepoApi.getRepo(owner, repoName)
      .toObservable()
      .map { RepoConverter.convertToDetail(it) }
      .concatMap { detail ->
        Observable.just(detail)
          .concatWith(
            gitHubRepoApi.getPulls(owner, repoName)
              .map { RepoConverter.convertRepos(detail, it) }
              .onErrorReturn { RepoConverter.convertRepos(detail, it) }
          )
      }
  }
}
