package com.jraska.github.client.users

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.jraska.github.client.http.ReplayHttpComponent
import com.jraska.github.client.ui.UsersActivity
import okreplay.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UsersActivityFlowTest {

  @get:Rule
  val testRule = ReplayHttpComponent.okReplayRule(UsersActivity::class.java)

  @Test
  @OkReplay
  fun whenStarts_thenDisplaysUsers() {
    onView(withText("defunkt")).perform(click())
    onView(withText("dotjs")).check(matches(isDisplayed()))
    onView(withText("facebox")).perform(click())
    onView(withText("1941")).check(matches(isDisplayed()))
  }
}
