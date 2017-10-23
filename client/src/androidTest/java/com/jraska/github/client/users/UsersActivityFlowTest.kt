package com.jraska.github.client.users

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.jraska.github.client.ui.UsersActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsersActivityFlowTest {
  @get:Rule
  var activityRule = ActivityTestRule(UsersActivity::class.java)

  @Test
  @Throws(InterruptedException::class)
  fun whenStarts_thenDisplaysUsers() {
    activityRule.activity

    onView(withText("defunkt")).perform(click())
    onView(withText("dotjs")).check(matches(isDisplayed()))
  }
}
