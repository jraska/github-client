package com.jraska.github.client.users.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GitHubUsersApi {
  @GET("/users")
  fun getUsers(@Query("since") since: Int): Call<List<GitHubUser>>

  @GET("/users/{login}")
  fun getUserDetail(@Path("login") login: String): Call<GitHubUserDetail>

  @GET("/users/{login}/repos?type=all")
  fun getRepos(@Path("login") login: String): Call<List<GitHubUserRepo>>
}
