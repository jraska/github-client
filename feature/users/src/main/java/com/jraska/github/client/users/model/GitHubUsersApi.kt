package com.jraska.github.client.users.model

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GitHubUsersApi {
  @GET("/users")
  fun getUsers(@Query("since") since: Int): Single<List<GitHubUser>>

  @GET("/users/{login}")
  fun getUserDetail(@Path("login") login: String): Single<GitHubUserDetail>

  @GET("/users/{login}/repos?type=all")
  fun getRepos(@Path("login") login: String): Single<List<GitHubUserRepo>>
}
