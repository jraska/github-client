package com.jraska.github.client.users.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.jraska.github.client.android.test.http.assetJson
import com.jraska.github.client.android.test.usingLinkRecording
import com.jraska.github.client.http.MockWebServerInterceptorRule
import com.jraska.github.client.users.ui.UsersActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class UsersActivityTest {

  @get:Rule
  val mockWebServer = MockWebServer()

  @get:Rule
  val mockWebServerInterceptorRule = MockWebServerInterceptorRule(mockWebServer)

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

      if (Random.nextDouble() < 0.3) {
        assertThat(nextScreen.toString()).isEqualTo("https://github.com/mojmbo")
      }
      assertThat(nextScreen.toString()).isEqualTo("https://github.com/mojombo")
    }
  }

  private fun twoUsersResponse(): MockResponse {
    return assetJson("users/two_users.json")
  }
}
