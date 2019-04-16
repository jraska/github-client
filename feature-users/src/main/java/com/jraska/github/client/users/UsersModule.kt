package com.jraska.github.client.users

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.jraska.github.client.Config
import com.jraska.github.client.Navigator
import com.jraska.github.client.PerApp
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.users.ui.UsersActivity

import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import okhttp3.HttpUrl
import retrofit2.Retrofit

@Module
object UsersModule {
  @JvmStatic
  @Provides
  @PerApp
  internal fun provideUsersRepository(retrofit: Retrofit): UsersRepository {
    val usersApi = retrofit.create(GitHubUsersApi::class.java)
    val detailApi = retrofit.create(GitHubUserDetailApi::class.java)

    return GitHubApiUsersRepository(usersApi, detailApi)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(UsersViewModel::class)
  internal fun provideUsersModel(
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
  internal fun provideUserDetailModel(
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
  internal fun provideRepoDetailModel(
    repository: UsersRepository,
    schedulers: AppSchedulers,
    navigator: Navigator,
    analytics: EventAnalytics
  ): ViewModel {
    return RepoDetailViewModel(repository, schedulers, navigator, analytics)
  }

  @JvmStatic
  @Provides
  @IntoSet
  internal fun provideUsersPathLauncher(): LinkLauncher {
    return UsersPathLauncher()
  }

  @JvmStatic
  @Provides
  @IntoSet
  internal fun provideUsersListLauncher(): LinkLauncher {
    return object : LinkLauncher {
      override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
        return if ("/users" == deepLink.encodedPath()) {
          UsersActivity.start(inActivity)
          LinkLauncher.Result.LAUNCHED
        } else {
          LinkLauncher.Result.NOT_LAUNCHED
        }
      }

      override fun priority(): LinkLauncher.Priority {
        return LinkLauncher.Priority.EXACT_MATCH
      }
    }
  }
}
