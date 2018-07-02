package com.jraska.github.client.users

import com.jraska.github.client.PerApp
import com.jraska.github.client.users.UsersRepository

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object UsersDataModule {
  @JvmStatic
  @Provides
  @PerApp
  fun provideUsersRepository(retrofit: Retrofit): UsersRepository {
    val usersApi = retrofit.create(GitHubUsersApi::class.java)
    val detailApi = retrofit.create(GitHubUserDetailApi::class.java)

    return GitHubApiUsersRepository(usersApi, detailApi)
  }
}
