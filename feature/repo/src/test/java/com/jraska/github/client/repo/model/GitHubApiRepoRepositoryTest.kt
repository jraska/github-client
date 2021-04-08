package com.jraska.github.client.repo.model

import com.jraska.github.client.http.HttpTest
import com.jraska.github.client.http.enqueue
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.Instant

internal class GitHubApiRepoRepositoryTest {

  @get:Rule
  val mockWebServer = MockWebServer()

  @Test
  fun getsTheDetailProperly() {
    val gitHubApiRepoRepository = GitHubApiRepoRepository(repoGitHubApi())
    mockWebServer.enqueue("response/repo_detail.json")
    mockWebServer.enqueue("response/repo_pulls.json")

    val repoDetail = gitHubApiRepoRepository.getRepoDetail("jraska", "github-client")
      .test()
      .assertValueCount(2)
      .values()
      .last()

    assertThat(repoDetail).usingRecursiveComparison().isEqualTo(expectedRepoDetail())
  }

  private fun repoGitHubApi() = HttpTest.retrofit(mockWebServer.url("/")).create(RepoGitHubApi::class.java)

  companion object {
    fun expectedRepoDetail(): RepoDetail {
      return RepoDetail(
        RepoHeader(
          "jraska",
          "github-client",
          "Experimental architecture app with example usage intended to be a showcase, test and skeleton app.",
          102,
          14
        ),
        RepoDetail.Data(Instant.parse("2016-03-01T23:38:14Z"), 4, "Kotlin", 3),
        RepoDetail.PullRequestsState.PullRequests(
          listOf(
            RepoDetail.PullRequest("Bump epoxy from 4.4.3 to 4.4.4"),
            RepoDetail.PullRequest("Bump kotlin_version from 1.4.31 to 1.4.32")
          )
        )
      )
    }
  }
}
