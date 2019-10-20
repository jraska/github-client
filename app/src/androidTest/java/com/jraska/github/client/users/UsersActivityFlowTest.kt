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
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.analytics.FirebaseAnalytics
import com.jraska.github.client.R
import com.jraska.github.client.TestUITestApp
import com.jraska.github.client.http.ReplayHttpComponent
import okreplay.OkReplay
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class UsersActivityFlowTest {

  @Suppress("unused")
  @get:Rule
  val testRule = ReplayHttpComponent.okReplayRule()

  @Test
  @OkReplay
  fun whenStarts_thenDisplaysUsers() {
    onView(withText("defunkt")).perform(click())
    onView(withText("dotjs")).check(matches(isDisplayed()))
    onView(withText("facebox")).perform(click())
    onView(withText("1941")).check(matches(isDisplayed()))
  }

  @Test
  @OkReplay
  fun whenSettings_thenReportsEvent() {
    onView(withId(R.id.action_settings)).perform(click())
    onView(withHint("Value")).perform(ViewActions.typeText("0.01"))
    onView(withText("Purchase")).perform(click())

    val testUITestApp = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestUITestApp
    val event = testUITestApp.coreComponent.eventAnalytics.events().findLast { event -> event.name == FirebaseAnalytics.Event.ECOMMERCE_PURCHASE }
    assertThat(event).isNotNull
    assertThat(event!!.properties[FirebaseAnalytics.Param.VALUE]).isEqualTo(0.01)
  }

  @Test
  @OkReplay
  fun whenAbout_thenOpensAbout() {
    onView(withId(R.id.action_about)).perform(click())

    onView(withText("by Josef Raska")).check(matches(isDisplayed()))
  }

  @Test
  @OkReplay
  fun whenRefreshes_thenDisplaysOtherUsers() {
    onView(withText("defunkt")).check(matches(isDisplayed()))
    onView(withId(R.id.users_refresh_swipe_layout)).perform(swipeDown())
    onView(withText("defunkt")).check(doesNotExist())
  }
}
