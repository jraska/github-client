package com.jraska.github.client.release

import com.jraska.github.client.release.data.GitHubApiFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.time.Instant

class GitHubApiImplTest {

  private lateinit var gitHubApi: GitHubApi

  @get:Rule
  val mockWebServer = MockWebServer()

  @Before
  fun setUp() {
    val environment = Environment("fakeToken", mockWebServer.url("/"))
    gitHubApi = GitHubApiFactory.create(environment)
  }

  @Test
  fun testPrMarkedProperly() {
    mockWebServer.enqueue("response/release.json")
    mockWebServer.enqueue(MockResponse().setResponseCode(200))

    gitHubApi.setReleaseBody("0.23.0", "Hey hallo")

    assertThat(mockWebServer.takeRequest().requestUrl!!.encodedPath).endsWith("/releases/tags/0.23.0")

    val secondRequest = mockWebServer.takeRequest()
    assertThat(secondRequest.requestUrl!!.encodedPath.endsWith("releases/40105170"))
    assertThat(secondRequest.body.toString()).contains("{\"body\":\"Hey hallo\"}")
  }

  @Test
  fun testGetsCommits() {
    mockWebServer.enqueue("response/commits_pr472.json")

    val prCommits = gitHubApi.prCommits(472)

    assertThat(mockWebServer.takeRequest().requestUrl!!.encodedPath).endsWith("/pulls/472/commits")

    val expectedCommit = Commit(
      "65910afb3de84bb52283fbc8cb0c4be0988d4343", Instant.parse("2021-04-09T22:37:33Z"),
      "jraska", "Delete test which isn't needed anymore",472
    )
    assertThat(prCommits[1]).usingRecursiveComparison().isEqualTo(expectedCommit)
  }
}

fun MockWebServer.enqueue(path: String) {
  enqueue(MockResponse().setResponseCode(200).setBody(json(path)))
}

fun json(path: String): String {
  val uri = GitHubApiImplTest::class.java.classLoader.getResource(path)
  val file = File(uri?.path!!)
  return String(file.readBytes())
}
