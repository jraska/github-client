package com.jraska.github.client.repo.model

import com.jraska.github.client.coroutines.result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class GitHubApiRepoRepository(
  private val gitHubRepoApi: RepoGitHubApi
) : RepoRepository {

  override fun getRepoDetail(owner: String, repoName: String): Flow<RepoDetail> {
    return flow {
      val repo = gitHubRepoApi.getRepo(owner, repoName).result()
      val firstDetail = RepoConverter.convertToDetail(repo)
      emit(firstDetail)

      try {
        val pulls = gitHubRepoApi.getPulls(owner, repoName).result()
        val secondDetail = RepoConverter.convertRepos(firstDetail, pulls)

        emit(secondDetail)
      } catch (ex: Exception) {
        emit(RepoConverter.convertRepos(firstDetail, ex))
      }
    }
  }
}
