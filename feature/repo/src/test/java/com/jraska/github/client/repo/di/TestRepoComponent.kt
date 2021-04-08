package com.jraska.github.client.repo.di

import com.jraska.github.client.FakeModules
import com.jraska.github.client.android.FakeSnackbarDisplay
import com.jraska.github.client.repo.RepoDetailViewModel
import com.jraska.github.client.repo.RepoModule
import dagger.Component
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Singleton
@Component(modules = [RepoModule::class, FakeModules::class])
internal interface TestRepoComponent {
  fun repoDetailViewModel(): RepoDetailViewModel

  val mockWebServer: MockWebServer

  val fakeSnackbarDisplay: FakeSnackbarDisplay
}
