package com.jraska.github.client.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.github.client.http.MockWebServerInterceptorRule
import com.jraska.github.client.http.enqueue
import com.jraska.github.client.repo.di.DaggerTestRepoComponent
import com.jraska.github.client.repo.di.TestRepoComponent
import com.jraska.github.client.repo.model.GitHubApiRepoRepositoryTest
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class RepoDetailViewModelTest {

  @get:Rule
  val testRule = InstantTaskExecutorRule()

  @get:Rule val mockWebServer = MockWebServer()

  @get:Rule val mockWebServerInterceptorRule = MockWebServerInterceptorRule(mockWebServer)

  lateinit var component: TestRepoComponent
  lateinit var repoDetailViewModel: RepoDetailViewModel

  @Before
  fun setUp() {
    component = DaggerTestRepoComponent.create()
    repoDetailViewModel = component.repoDetailViewModel()
  }

  @Test
  fun whenLoadThenLoadsProperRepoDetail() {
    mockWebServer.enqueue("response/repo_detail.json")
    mockWebServer.enqueue("response/repo_pulls.json")

    val showRepo = runBlocking {
      repoDetailViewModel.stateFlow("hello/vole").toCollection(mutableListOf())
    }[1] as RepoDetailViewModel.ViewState.ShowRepo

    assertThat(showRepo.repo).usingRecursiveComparison().isEqualTo(GitHubApiRepoRepositoryTest.expectedRepoDetail())

//    runBlocking {
//      val showRepo = repoDetailViewModel.repoDetail("jraska/github-client")
//        .test()
//        .value() as RepoDetailViewModel.ViewState.ShowRepo
//
//      assertThat(showRepo.repo).usingRecursiveComparison().isEqualTo(GitHubApiRepoRepositoryTest.expectedRepoDetail())
//    }
  }

  @Test
  fun whenClicksThenOpensGitHub() {
    repoDetailViewModel.onGitHubIconClicked("jraska/github-client")

    assertThat(component.fakeSnackbarDisplay.snackbarsInvoked().last().text).isEqualTo(R.string.repo_detail_open_web_text)
  }

  @Test
  fun whenErrorThenLoadsErrorState() {
    mockWebServer.enqueue("response/error.json")
    mockWebServer.enqueue("response/error.json")

    val state = runBlocking {
      repoDetailViewModel.stateFlow("jraska/github-client").toCollection(mutableListOf())[1]
    }

//    val state = repoDetailViewModel.repoDetail("jraska/github-client")
//      .test()
//      .value()

    assertThat(state).isInstanceOf(RepoDetailViewModel.ViewState.Error::class.java)
  }
}
