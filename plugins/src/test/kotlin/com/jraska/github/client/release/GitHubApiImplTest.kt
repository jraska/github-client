package com.jraska.github.client.release

import com.jraska.github.client.release.data.GitHubApiFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class GitHubApiImplTest {

  @get:Rule
  val mockWebServer = MockWebServer()

  @Test
  fun testPrMarkedProperly() {
    val environment = Environment("fakeToken", mockWebServer.url("/"))
    val gitHubApi = GitHubApiFactory.create(environment)

    mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{\"url\":\"https://api.github.com/repos/jraska/github-client/releases/40105170\",\"assets_url\":\"https://api.github.com/repos/jraska/github-client/releases/40105170/assets\",\"upload_url\":\"https://uploads.github.com/repos/jraska/github-client/releases/40105170/assets{?name,label}\",\"html_url\":\"https://github.com/jraska/github-client/releases/tag/0.23.0\",\"id\":40105170,\"author\":{\"login\":\"jraska\",\"id\":6277721,\"node_id\":\"MDQ6VXNlcjYyNzc3MjE=\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/6277721?v=4\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/jraska\",\"html_url\":\"https://github.com/jraska\",\"followers_url\":\"https://api.github.com/users/jraska/followers\",\"following_url\":\"https://api.github.com/users/jraska/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/jraska/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/jraska/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/jraska/subscriptions\",\"organizations_url\":\"https://api.github.com/users/jraska/orgs\",\"repos_url\":\"https://api.github.com/users/jraska/repos\",\"events_url\":\"https://api.github.com/users/jraska/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/jraska/received_events\",\"type\":\"User\",\"site_admin\":false},\"node_id\":\"MDc6UmVsZWFzZTQwMTA1MTcw\",\"tag_name\":\"0.23.0\",\"target_commitish\":\"master\",\"name\":\"0.23.0\",\"draft\":false,\"prerelease\":false,\"created_at\":\"2021-03-20T16:30:43Z\",\"published_at\":\"2021-03-20T16:31:04Z\",\"assets\":[],\"tarball_url\":\"https://api.github.com/repos/jraska/github-client/tarball/0.23.0\",\"zipball_url\":\"https://api.github.com/repos/jraska/github-client/zipball/0.23.0\",\"body\":\"Hey hey\"}\n"))
    mockWebServer.enqueue(MockResponse().setResponseCode(200))

    gitHubApi.setReleaseBody("0.23.0", "Hey hallo")

    assertThat(mockWebServer.takeRequest().requestUrl!!.encodedPath).endsWith("/releases/tags/0.23.0")

    val secondRequest = mockWebServer.takeRequest()
    assertThat(secondRequest.requestUrl!!.encodedPath.endsWith("releases/40105170"))
    assertThat(secondRequest.body.toString()).contains("{\"body\":\"Hey hallo\"}")
  }
}
