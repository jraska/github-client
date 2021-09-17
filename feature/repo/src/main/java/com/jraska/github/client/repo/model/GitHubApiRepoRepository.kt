package com.jraska.github.client.repo.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call

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

  fun <T> Call<T>.result(): T {
    val response = execute()
    if (response.isSuccessful) {
      return response.body()!!
    } else {
      throw retrofit2.HttpException(response)
    }
  }
}
