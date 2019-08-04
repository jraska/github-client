package com.jraska.github.client.users.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

internal interface GitHubUserDetailApi {
  @GET("/users/{login}")
  fun getUserDetail(@Path("login") login: String): Single<GitHubUserDetail>

  @GET("/users/{login}/repos?type=all")
  fun getRepos(@Path("login") login: String): Single<List<GitHubRepo>>
}
