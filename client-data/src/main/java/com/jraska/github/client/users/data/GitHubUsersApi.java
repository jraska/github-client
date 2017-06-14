package com.jraska.github.client.users.data;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface GitHubUsersApi {
  @GET("/users") Single<List<GitHubUser>> getUsers(@Query("since") int since);

  @GET("/repos/{owner}/{name}")
  Single<GitHubRepo> getRepo(@Path("owner") String path, @Path("name") String name);
}
