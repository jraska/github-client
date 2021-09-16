package com.jraska.github.client.repo.model

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

  override fun getRepoDetailFlow(owner: String, repoName: String): Flow<RepoDetail> {
    return flow {
      val repo = gitHubRepoApi.getRepoCoroutine(owner, repoName).execute().body()!!
      val firstDetail = RepoConverter.convertToDetail(repo)
      emit(firstDetail)

      try {
        val pulls = gitHubRepoApi.getPullsCoroutine(owner, repoName).execute().body()!!
        val secondDetail = RepoConverter.convertRepos(firstDetail, pulls)

        emit(secondDetail)
      } catch (ex: Exception) {
        emit(RepoConverter.convertRepos(firstDetail, ex))
      }
    }
  }
}
