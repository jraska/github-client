package com.jraska.github.client.users

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import com.google.firebase.analytics.FirebaseAnalytics
import com.jraska.github.client.R
import com.jraska.github.client.TestUITestApp
import com.jraska.github.client.http.ReplayHttpComponent
import okreplay.OkReplay
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
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
    onView(withId(R.id.settings_purchase_input)).perform(ViewActions.typeText("0.01"))
    onView(withId(R.id.settings_purchase_submit_button)).perform(click())

    val testUITestApp = InstrumentationRegistry.getTargetContext().applicationContext as TestUITestApp
    val event = testUITestApp.coreComponent.eventAnalytics.events().findLast { event -> event.name == FirebaseAnalytics.Event.ECOMMERCE_PURCHASE }
    assertThat(event).isNotNull
    assertThat(event!!.properties[FirebaseAnalytics.Param.VALUE]).isEqualTo(0.01)
  }

  @Test
  @OkReplay
  fun whenRefreshes_thenDisplaysOtherUsers() {
    onView(withText("defunkt")).check(matches(isDisplayed()))
    onView(withId(R.id.users_refresh_swipe_layout)).perform(swipeDown())
    onView(withText("defunkt")).check(doesNotExist())
  }
}
