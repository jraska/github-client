package com.jraska.github.client.repo.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RepoGitHubApi {
  @GET("/repos/{owner}/{name}")
  fun getRepo(@Path("owner") path: String, @Path("name") name: String): Single<GitHubRepo>

  @GET("/repos/{owner}/{name}/pulls?state=all")
  fun getPulls(@Path("owner") path: String, @Path("name") name: String): Single<List<GitHubPullRequest>>
}
