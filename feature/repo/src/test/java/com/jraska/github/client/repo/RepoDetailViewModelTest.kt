package com.jraska.github.client.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.github.client.http.enqueue
import com.jraska.github.client.repo.di.DaggerTestRepoComponent
import com.jraska.github.client.repo.di.TestRepoComponent
import com.jraska.github.client.repo.model.GitHubApiRepoRepositoryTest
import com.jraska.livedata.test
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class RepoDetailViewModelTest {

  @get:Rule
  val testRule = InstantTaskExecutorRule()

  lateinit var component: TestRepoComponent
  lateinit var repoDetailViewModel: RepoDetailViewModel

  @Before
  fun setUp() {
    component = DaggerTestRepoComponent.create()
    repoDetailViewModel = component.repoDetailViewModel()
  }

  @Test
  fun whenLoad_thenLoadsProperRepoDetail() {
    component.mockWebServer().enqueue("response/repo_detail.json")
    component.mockWebServer().enqueue("response/repo_pulls.json")

    val showRepo = repoDetailViewModel.repoDetail("jraska/github-client")
      .test()
      .value() as RepoDetailViewModel.ViewState.ShowRepo

    assertThat(showRepo.repo).usingRecursiveComparison().isEqualTo(GitHubApiRepoRepositoryTest.expectedRepoDetail())
  }

  @Test
  fun whenError_thenLoadsErrorState() {
    component.mockWebServer().enqueue("response/error.json")
    component.mockWebServer().enqueue("response/error.json")

    val state = repoDetailViewModel.repoDetail("jraska/github-client")
      .test()
      .value()

    assertThat(state).isInstanceOf(RepoDetailViewModel.ViewState.Error::class.java)
  }
}
