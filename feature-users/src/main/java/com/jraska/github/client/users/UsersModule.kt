package com.jraska.github.client.users

import androidx.lifecycle.ViewModel
import com.jraska.github.client.Config
import com.jraska.github.client.Navigator
import com.jraska.github.client.PerApp
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.rx.AppSchedulers

import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
object UsersModule {
  @JvmStatic
  @Provides
  @PerApp
  fun provideUsersRepository(retrofit: Retrofit): UsersRepository {
    val usersApi = retrofit.create(GitHubUsersApi::class.java)
    val detailApi = retrofit.create(GitHubUserDetailApi::class.java)

    return GitHubApiUsersRepository(usersApi, detailApi)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(UsersViewModel::class)
  fun provideUsersModel(
    repository: UsersRepository,
    schedulers: AppSchedulers,
    navigator: Navigator,
    analytics: EventAnalytics
  ): ViewModel {
    return UsersViewModel(repository, schedulers, navigator, analytics)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(UserDetailViewModel::class)
  fun provideUserDetailModel(
    repository: UsersRepository,
    schedulers: AppSchedulers,
    navigator: Navigator,
    analytics: EventAnalytics,
    config: Config
  ): ViewModel {
    return UserDetailViewModel(repository, schedulers, navigator, analytics, config)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(RepoDetailViewModel::class)
  fun provideRepoDetailModel(
    repository: UsersRepository,
    schedulers: AppSchedulers,
    navigator: Navigator,
    analytics: EventAnalytics
  ): ViewModel {
    return RepoDetailViewModel(repository, schedulers, navigator, analytics)
  }
}
