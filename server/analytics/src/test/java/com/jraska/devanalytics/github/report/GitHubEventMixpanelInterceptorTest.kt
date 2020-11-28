package com.jraska.devanalytics.github.report

import com.jraska.devanalytics.github.EventReaderTest
import com.jraska.devanalytics.github.model.Environment
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class GitHubEventMixpanelInterceptorTest {
  @get:Rule
  val mockWebServer = MockWebServer()

  @Test
  fun reportsToFakeServer() {
    val fakeEnvironment = Environment(mockWebServer.url("/mixpanel/").toString(), "fakeToken")
    val interceptor = GitHubEventMixpanelInterceptor.create(fakeEnvironment)

    mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("1"))

    val event = interceptor.intercept(EventReaderTest.json("response/pr_comment.json"))

    assertThat(event.name).isEqualTo("PR Comment")
    assertThat(event.action).isEqualTo("created")
    assertThat(event.author).isEqualTo("jraska")
    assertThat(event.prUrl).isEqualTo("https://github.com/jraska/github-client/pull/353")
    assertThat(event.prNumber).isEqualTo(353)
    assertThat(event.comment).isEqualTo("Another test comment")
  }
}
