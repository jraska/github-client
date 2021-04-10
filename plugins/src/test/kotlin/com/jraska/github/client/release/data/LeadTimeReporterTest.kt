package com.jraska.github.client.release.data

import com.jraska.analytics.AnalyticsEvent
import com.jraska.analytics.AnalyticsReporter
import com.jraska.github.client.release.Environment
import com.jraska.github.client.release.PullRequest
import com.jraska.github.client.release.Release
import com.jraska.github.client.release.enqueue
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant

class LeadTimeReporterTest {
  private lateinit var leadTimeReporter: LeadTimeReporter
  lateinit var analyticsReporter: RecordingAnalyticsReporter

  @get:Rule
  val mockWebServer = MockWebServer()

  @Before
  fun setUp() {
    analyticsReporter = RecordingAnalyticsReporter()
    val environment = Environment("fakeToken", mockWebServer.url("/"))
    val gitHubApi = GitHubApiFactory.create(environment)

    val release = Release("testRelease", "https://jrasks.com".toHttpUrl(), Instant.parse("2021-04-10T12:00:00Z"))
    leadTimeReporter = LeadTimeReporter(gitHubApi, release, analyticsReporter)
  }

  @Test
  fun testReportsLeadTimeProperly() {
    mockWebServer.enqueue("response/commits_pr472.json")
    mockWebServer.enqueue("response/commits_pr460.json")

    leadTimeReporter.reportLeadTime(listOf(PullRequest(472, "First PR"), PullRequest(460, "Second PR")))

    assertThat(mockWebServer.requestCount).isEqualTo(2)

    val reportedEvents = analyticsReporter.events()
    assertThat(reportedEvents).hasSize(3)

    assertThat(reportedEvents).allMatch { it.name == "Commit Released" }
    assertThat(reportedEvents).allMatch { it.properties["releaseName"] == "testRelease" }

    val first = reportedEvents.first()
    assertThat(first.properties["leadTimeSec"]).isEqualTo(13 * 3600 + 24 * 60 + 53L)
    assertThat(first.properties["author"]).isEqualTo("jraska")
    assertThat(first.properties["gitCommit"]).isEqualTo("859ffe735dc185336cbcad09e692d45dcf8c3361")
    assertThat(first.properties["message"]).isEqualTo("un composite build tests on CI")

    val second = reportedEvents[1]
    assertThat(second.properties["leadTimeSec"]).isEqualTo(13 * 3600 + 22 * 60 + 27L)
    assertThat(second.properties["author"]).isEqualTo("jraska")
    assertThat(second.properties["gitCommit"]).isEqualTo("65910afb3de84bb52283fbc8cb0c4be0988d4343")
    assertThat(second.properties["message"]).isEqualTo("Delete test which isn't needed anymore")

    val third = reportedEvents[2]
    assertThat(third.properties["leadTimeSec"]).isEqualTo(11 * 24 * 3600 + 23 * 3600 + 24 * 60 + 0L)
    assertThat(third.properties["author"]).isEqualTo("dependabot[bot]")
    assertThat(third.properties["gitCommit"]).isEqualTo("70e59ee5f812506651b10effbb00657d8e7ca4b2")
  }
}

class RecordingAnalyticsReporter : AnalyticsReporter {
  private val recorder: MutableList<AnalyticsEvent> = mutableListOf()

  fun events(): List<AnalyticsEvent> = recorder

  override fun report(vararg events: AnalyticsEvent) {
    recorder.addAll(events)
  }
}
