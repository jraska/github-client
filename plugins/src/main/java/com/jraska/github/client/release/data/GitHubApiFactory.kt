package com.jraska.github.client.release.data

import com.jraska.github.client.release.GitHubApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException

object GitHubApiFactory {
  fun create(): GitHubApi {
    val apiToken = System.getenv("TOKEN_GITHUB_API") ?: throw IllegalStateException("GitHub API token missing")

    val client = OkHttpClient.Builder()
      .addInterceptor(GitHubInterceptor(apiToken))
      .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
      .build()

    val retrofit = Retrofit.Builder()
      .baseUrl("https://api.github.com/repos/jraska/github-client/")
      .client(client)
      .validateEagerly(true)
      .addConverterFactory(GsonConverterFactory.create())
      .build()

    return GitHubApiImpl(retrofit.create(RetrofitGitHubApi::class.java))
  }
}
