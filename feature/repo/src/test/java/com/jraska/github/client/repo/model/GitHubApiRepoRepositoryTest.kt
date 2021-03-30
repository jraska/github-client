package com.jraska.github.client.repo.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.Instant
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class GitHubApiRepoRepositoryTest {

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

  // This needs to be easier to create in tests
  private fun repoGitHubApi() = Retrofit.Builder()
    .baseUrl(mockWebServer.url("/"))
    .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor { println(it) }.apply {
      level = HttpLoggingInterceptor.Level.BASIC
    }).build())
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()
    .create(RepoGitHubApi::class.java)

  private fun expectedRepoDetail(): RepoDetail {
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

  fun MockWebServer.enqueue(path: String) {
    enqueue(MockResponse().setBody(json(path)))
  }

  @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
  private fun MockWebServer.json(path: String): String {
    val uri = this.javaClass.classLoader.getResource(path)
    val file = File(uri?.path!!)
    return String(file.readBytes())
  }
}
