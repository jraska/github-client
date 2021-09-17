package com.jraska.github.client.users

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import com.jraska.github.client.config.MutableConfigDef
import com.jraska.github.client.config.MutableConfigSetup
import com.jraska.github.client.config.MutableConfigType
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.core.android.OnAppCreateAsync
import com.jraska.github.client.coroutines.AppDispatchers
import com.jraska.github.client.users.model.GitHubApiUsersRepository
import com.jraska.github.client.users.model.GitHubUsersApi
import com.jraska.github.client.users.model.UsersRepository
import com.jraska.github.client.users.ui.UsersActivity
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import okhttp3.HttpUrl
import retrofit2.Retrofit
import javax.inject.Provider
import javax.inject.Singleton

@Module
object UsersModule {

  @Provides
  @Singleton
  internal fun provideUsersRepository(retrofit: Retrofit, appDispatchers: AppDispatchers): UsersRepository {
    val usersApi = retrofit.create(GitHubUsersApi::class.java)

    return GitHubApiUsersRepository(usersApi, appDispatchers)
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

  /**
   * This is an optimisation of the app startup - all features SHOULD NOT do this as it consumes app startup resources.
   * This is recommended only for features loaded at the app startup.
   */
  @Provides
  @IntoSet
  internal fun prepareHttpClient(usersRepositoryProvider: Provider<UsersRepository>): OnAppCreateAsync {
    return object : OnAppCreateAsync {
      override fun onCreateAsync(app: Application) {
        usersRepositoryProvider.get() // setups asynchronously the rest client
      }
    }
  }

  @Provides
  @IntoSet
  internal fun debugConfigs(): MutableConfigSetup {
    return object : MutableConfigSetup {
      override fun mutableConfigs(): List<MutableConfigDef> {
        return listOf(
          MutableConfigDef(
            UserDetailViewModel.USER_DETAIL_SECTION_SIZE_KEY,
            MutableConfigType.LONG,
            listOf(1, 2, 3, 4, 5, 7, 10)
          )
        )
      }
    }
  }
}
