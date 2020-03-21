package com.jraska.github.client.users

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.core.android.snackbar.SnackbarDisplay
import com.jraska.github.client.users.model.GitHubApiUsersRepository
import com.jraska.github.client.users.model.GitHubUserDetailApi
import com.jraska.github.client.users.model.GitHubUsersApi
import com.jraska.github.client.users.model.RepoDetailViewModel
import com.jraska.github.client.users.model.UsersRepository
import com.jraska.github.client.users.ui.UsersActivity
import com.jraska.github.client.users.widget.TopSnackbarDisplay
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import okhttp3.HttpUrl
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object UsersModule {

  @Provides
  @Singleton
  internal fun provideUsersRepository(retrofit: Retrofit): UsersRepository {
    val usersApi = retrofit.create(GitHubUsersApi::class.java)
    val detailApi = retrofit.create(GitHubUserDetailApi::class.java)

    return GitHubApiUsersRepository(usersApi, detailApi)
  }

  @Provides
  @IntoMap
  @ClassKey(UsersViewModel::class)
  internal fun provideUsersModel(viewModel: UsersViewModel): ViewModel {
    return viewModel
  }

  @Provides
  @IntoMap
  @ClassKey(UserDetailViewModel::class)
  internal fun provideUserDetailModel(viewModel: UserDetailViewModel): ViewModel {
    return viewModel
  }

  @Provides
  @IntoMap
  @ClassKey(RepoDetailViewModel::class)
  internal fun provideRepoDetailModel(viewModel: RepoDetailViewModel): ViewModel {
    return viewModel
  }

  @Provides
  @IntoSet
  internal fun provideUsersPathLauncher(): LinkLauncher {
    return UsersPathLauncher()
  }

  @Provides
  @IntoSet
  internal fun provideUsersListLauncher(): LinkLauncher {
    return object : LinkLauncher {
      override fun launch(inActivity: Activity, deepLink: HttpUrl): LinkLauncher.Result {
        return if ("/users" == deepLink.encodedPath) {
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

  @Provides
  fun snackbarDisplay(display: TopSnackbarDisplay): SnackbarDisplay = display
}
