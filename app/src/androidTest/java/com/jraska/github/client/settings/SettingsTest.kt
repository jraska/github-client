package com.jraska.github.client.settings

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.jraska.github.client.DeepLinkLaunchTest
import com.jraska.github.client.navigation.Urls
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class SettingsTest {
  @Test
  fun whenConsoleClicked_thenConsoleOpens() {
    DeepLinkLaunchTest.launchDeepLink(Urls.settings().toString())

    onView(withId(R.id.console_item_container)).perform(click())

    onView(withSubstring(Urls.console().toString())).check(matches(isDisplayed()))
    onView(withId(R.id.console_item_container)).check(matches(not(isDisplayed())))
  }
}
