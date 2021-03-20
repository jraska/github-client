package com.jraska.github.client.release.data

import com.jraska.github.client.release.Environment
import com.jraska.github.client.release.GitHubApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException

object GitHubApiFactory {
  fun create(environment: Environment): GitHubApi {
    val client = OkHttpClient.Builder()
      .addInterceptor(GitHubInterceptor(environment.apiToken))
      .addInterceptor(HttpLoggingInterceptor { println(it) }.setLevel(HttpLoggingInterceptor.Level.BASIC))
      .build()

    val retrofit = Retrofit.Builder()
      .baseUrl(environment.baseUrl)
      .client(client)
      .validateEagerly(true)
      .addConverterFactory(GsonConverterFactory.create())
      .build()

    return GitHubApiImpl(retrofit.create(RetrofitGitHubApi::class.java))
  }
}
