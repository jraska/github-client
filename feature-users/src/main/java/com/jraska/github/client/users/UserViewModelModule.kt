package com.jraska.github.client.users

import androidx.lifecycle.ViewModel
import com.jraska.github.client.Config
import com.jraska.github.client.Navigator
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.rx.AppSchedulers
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
object UserViewModelModule {
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
