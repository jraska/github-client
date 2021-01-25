package com.jraska.github.client.repo

import androidx.lifecycle.ViewModel
import com.jraska.github.client.core.android.LinkLauncher
import com.jraska.github.client.repo.model.GitHubApiRepoRepository
import com.jraska.github.client.repo.model.RepoGitHubApi
import com.jraska.github.client.repo.model.RepoRepository
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object RepoModule {
  @Provides
  @Singleton
  internal fun provideUsersRepository(retrofit: Retrofit): RepoRepository {
    val gitHubRepoApi = retrofit.create(RepoGitHubApi::class.java)
    return GitHubApiRepoRepository(gitHubRepoApi)
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
    return RepoPathLauncher()
  }
}
