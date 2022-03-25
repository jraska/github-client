package com.jraska.github.client.users

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.google.firebase.analytics.FirebaseAnalytics
import com.jraska.github.client.EnableConfigRule
import com.jraska.github.client.android.test.http.assetJson
import com.jraska.github.client.espressox.LongClickPatch
import com.jraska.github.client.http.MockWebServerInterceptorRule
import com.jraska.github.client.http.onUrlPartReturn
import com.jraska.github.client.http.onUrlReturn
import com.jraska.github.client.recordedEvents
import com.jraska.github.client.users.R
import com.jraska.github.client.users.ui.UsersActivity
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class UsersActivityFlowTest {

  @get:Rule val mockWebServer = MockWebServer()

  @get:Rule val mockWebServerInterceptorRule = MockWebServerInterceptorRule(mockWebServer)

  @get:Rule
  val rule = ActivityTestRule(UsersActivity::class.java, false, false)

  @get:Rule
  val enableConfigRule = EnableConfigRule("user_detail_section_size", 4L)

  @Test
  fun whenStartsThenDisplaysUsers() {
    mockWebServer.onUrlReturn(".*/users".toRegex(), assetJson("response/users.json"))
    mockWebServer.onUrlReturn(".*/users/mojombo".toRegex(), assetJson("response/defunkt.json"))
    mockWebServer.onUrlPartReturn("users/mojombo/repos", assetJson("response/defunkt_repos.json"))
    mockWebServer.onUrlPartReturn("repos/defunkt/hurl", assetJson("response/repo_hurl.json"))

    rule.launchActivity(null)

    onView(withText("mojombo")).perform(click())
    onView(withText("charlock_holmes")).check(matches(isDisplayed()))
    onView(withText("hurl")).perform(click())
    onView(withText("523")).check(matches(isDisplayed()))
  }

  @Test
  fun whenSettingsThenReportsEvent() {
    rule.launchActivity(null)
    mockWebServer.enqueue(assetJson("response/users.json"))

    onView(withId(R.id.action_settings)).perform(click(LongClickPatch))
    onView(withHint("Value")).perform(ViewActions.typeText("0.01"))
    onView(withText("Purchase")).perform(click())

    val event = recordedEvents().findLast { event -> event.name == FirebaseAnalytics.Event.ECOMMERCE_PURCHASE }
    assertThat(event).isNotNull
    assertThat(event!!.properties[FirebaseAnalytics.Param.VALUE]).isEqualTo(0.01)
  }

  @Test
  fun whenAboutThenOpensAbout() {
    rule.launchActivity(null)
    mockWebServer.enqueue(assetJson("response/users.json"))

    onView(withId(R.id.action_about)).perform(click(LongClickPatch))

    onView(withText("by Josef Raska")).check(matches(isDisplayed()))
  }

  @Test
  fun whenRefreshesThenDisplaysOtherUsers() {
    rule.launchActivity(null)
    mockWebServer.enqueue(assetJson("response/users.json"))
    mockWebServer.enqueue(assetJson("response/users_no_defunkt.json"))

    onView(withText("defunkt")).check(matches(isDisplayed()))
    onView(withId(R.id.users_refresh_swipe_layout)).perform(swipeDown())
    onView(withText("defunkt")).check(doesNotExist())
  }
}
