package com.jraska.github.client

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.jraska.github.client.android.test.http.assetJson
import com.jraska.github.client.http.MockWebServerInterceptorRule
import com.jraska.github.client.http.onUrlPartReturn
import com.jraska.github.client.http.onUrlReturn
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

class DeepLinkLaunchTest {

  @get:Rule val mockWebServer = MockWebServer()

  @get:Rule val mockWebServerInterceptorRule = MockWebServerInterceptorRule(mockWebServer)

  @Test
  fun whenUsersLinkThenUsersActivityDisplayed() {
    mockWebServer.enqueue(assetJson("response/users.json"))

    launchDeepLink("https://github.com/users")

    onView(withText("defunkt")).check(matches(isDisplayed()))
    onView(withText("mojombo")).check(matches(isDisplayed()))
  }

  @Test
  fun whenDetailLinkThenUserDetailActivityDisplayed() {
    mockWebServer.onUrlReturn(".*/users/mojombo".toRegex(), assetJson("response/mojombo.json"))
    mockWebServer.onUrlPartReturn("users/mojombo/repos", assetJson("response/mojombo_repos.json"))

    launchDeepLink("https://github.com/mojombo")

    onView(withText("charlock_holmes")).check(matches(isDisplayed()))
  }

  @Test
  fun whenRepoLinkThenRepoActivityDisplayed() {
    mockWebServer.enqueue(assetJson("response/repo_detail.json"))
    mockWebServer.enqueue(assetJson("response/repo_pulls.json"))

    launchDeepLink("https://github.com/jraska/Falcon")

    onView(withText(Matchers.containsString("Language: Java")))
      .check(matches(isDisplayed()))

    onView(withId(R.id.repo_detail_github_fab)).perform(click())
    onView(withText("Open"))
      .check(matches(isDisplayed()))
  }

  companion object {
    fun launchDeepLink(deepLink: String) {
      val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

      val intent = Intent(Intent.ACTION_VIEW)
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      intent.`package` = targetContext.packageName
      intent.data = Uri.parse(deepLink)
      targetContext.startActivity(intent)
    }
  }
}
