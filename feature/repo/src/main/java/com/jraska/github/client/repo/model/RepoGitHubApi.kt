package com.jraska.github.client.repo.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

internal interface RepoGitHubApi {
  @GET("/repos/{owner}/{name}")
  fun getRepo(@Path("owner") path: String, @Path("name") name: String): Call<GitHubRepo>

  @GET("/repos/{owner}/{name}/pulls?state=all")
  fun getPulls(@Path("owner") path: String, @Path("name") name: String): Call<List<GitHubPullRequest>>
}
