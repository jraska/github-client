package com.jraska.github.client.users.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.jraska.github.client.http.MockWebServerInterceptor
import com.jraska.github.client.http.MockWebServerInterceptorRule
import com.jraska.github.client.users.ui.UsersActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UsersActivityTest {

  @get:Rule
  val mockWebServer = MockWebServer()

  @get:Rule val mockWebServerInterceptorRule = MockWebServerInterceptorRule(mockWebServer)

  @get:Rule
  val rule = ActivityTestRule(UsersActivity::class.java)

  @Test
  fun testLaunches() {
    mockWebServer.enqueue(twoUsersResponse())

    usingLinkRecording {
      onView(withText("defunkt")).perform(click())

      val lastScreen = it.linksLaunched.last()
      assertThat(lastScreen.toString()).isEqualTo("https://github.com/defunkt")

      onView(withText("mojombo")).perform(click())
      val nextScreen = it.linksLaunched.last()
      assertThat(nextScreen.toString()).isEqualTo("https://github.com/mojombo")
    }
  }

  private fun twoUsersResponse() = MockResponse().setResponseCode(200).setBody(
    "[\n" +
      "  {\n" +
      "    \"login\": \"mojombo\",\n" +
      "    \"id\": 1,\n" +
      "    \"node_id\": \"MDQ6VXNlcjE=\",\n" +
      "    \"avatar_url\": \"https://avatars.githubusercontent.com/u/1?v=4\",\n" +
      "    \"gravatar_id\": \"\",\n" +
      "    \"url\": \"https://api.github.com/users/mojombo\",\n" +
      "    \"html_url\": \"https://github.com/mojombo\",\n" +
      "    \"followers_url\": \"https://api.github.com/users/mojombo/followers\",\n" +
      "    \"following_url\": \"https://api.github.com/users/mojombo/following{/other_user}\",\n" +
      "    \"gists_url\": \"https://api.github.com/users/mojombo/gists{/gist_id}\",\n" +
      "    \"starred_url\": \"https://api.github.com/users/mojombo/starred{/owner}{/repo}\",\n" +
      "    \"subscriptions_url\": \"https://api.github.com/users/mojombo/subscriptions\",\n" +
      "    \"organizations_url\": \"https://api.github.com/users/mojombo/orgs\",\n" +
      "    \"repos_url\": \"https://api.github.com/users/mojombo/repos\",\n" +
      "    \"events_url\": \"https://api.github.com/users/mojombo/events{/privacy}\",\n" +
      "    \"received_events_url\": \"https://api.github.com/users/mojombo/received_events\",\n" +
      "    \"type\": \"User\",\n" +
      "    \"site_admin\": false\n" +
      "  },\n" +
      "  {\n" +
      "    \"login\": \"defunkt\",\n" +
      "    \"id\": 2,\n" +
      "    \"node_id\": \"MDQ6VXNlcjI=\",\n" +
      "    \"avatar_url\": \"https://avatars0.githubusercontent.com/u/2?v=4\",\n" +
      "    \"gravatar_id\": \"\",\n" +
      "    \"url\": \"https://api.github.com/users/defunkt\",\n" +
      "    \"html_url\": \"https://github.com/defunkt\",\n" +
      "    \"followers_url\": \"https://api.github.com/users/defunkt/followers\",\n" +
      "    \"following_url\": \"https://api.github.com/users/defunkt/following{/other_user}\",\n" +
      "    \"gists_url\": \"https://api.github.com/users/defunkt/gists{/gist_id}\",\n" +
      "    \"starred_url\": \"https://api.github.com/users/defunkt/starred{/owner}{/repo}\",\n" +
      "    \"subscriptions_url\": \"https://api.github.com/users/defunkt/subscriptions\",\n" +
      "    \"organizations_url\": \"https://api.github.com/users/defunkt/orgs\",\n" +
      "    \"repos_url\": \"https://api.github.com/users/defunkt/repos\",\n" +
      "    \"events_url\": \"https://api.github.com/users/defunkt/events{/privacy}\",\n" +
      "    \"received_events_url\": \"https://api.github.com/users/defunkt/received_events\",\n" +
      "    \"type\": \"User\",\n" +
      "    \"site_admin\": true\n" +
      "  }]"
  )
}
