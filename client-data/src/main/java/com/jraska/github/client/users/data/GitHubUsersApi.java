package com.jraska.github.client.users.data;

import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Single;

import java.util.List;

interface GitHubUsersApi {
  @GET("/users") Single<List<GitHubUser>> getUsers(@Query("since") int since);
}
