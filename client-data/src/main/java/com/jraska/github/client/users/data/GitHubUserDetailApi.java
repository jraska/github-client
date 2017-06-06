package com.jraska.github.client.users.data;

import retrofit2.http.GET;
import retrofit2.http.Path;
import io.reactivex.Single;

import java.util.List;

interface GitHubUserDetailApi {
  @GET("/users/{login}")
  Single<GitHubUserDetail> getUserDetail(@Path("login") String login);

  @GET("/users/{login}/repos?type=all")
  Single<List<GitHubRepo>> getRepos(@Path("login") String login);
}
