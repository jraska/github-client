package com.jraska.github.client.users.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GitHubUsersApi {
  @GET("/users")
  fun getUsers(@Query("since") since: Int): Single<List<GitHubUser>>

  @GET("/repos/{owner}/{name}")
  fun getRepo(@Path("owner") path: String, @Path("name") name: String): Single<GitHubRepo>
}
