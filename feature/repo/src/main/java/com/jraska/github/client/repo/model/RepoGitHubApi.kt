package com.jraska.github.client.repo.model

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

internal interface RepoGitHubApi {
  @GET("/repos/{owner}/{name}")
  fun getRepo(@Path("owner") path: String, @Path("name") name: String): Single<GitHubRepo>

  @GET("/repos/{owner}/{name}/pulls?state=all")
  fun getPulls(@Path("owner") path: String, @Path("name") name: String): Single<List<GitHubPullRequest>>

  @GET("/repos/{owner}/{name}")
  suspend fun getRepoCoroutine(@Path("owner") path: String, @Path("name") name: String): GitHubRepo

  @GET("/repos/{owner}/{name}/pulls?state=all")
  suspend fun getPullsCoroutine(@Path("owner") path: String, @Path("name") name: String): List<GitHubPullRequest>
}
