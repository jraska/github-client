package com.jraska.github.client.espressox

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher

// Action to do nothing and just retry again in case of accidental long tap.
// https://issuetracker.google.com/issues/37078920
object LongClickPatch : ViewAction {
  override fun getConstraints(): Matcher<View> {
    return isAssignableFrom(View::class.java)
  }

  override fun getDescription(): String {
    return "Handle tap->long click.";
  }

  override fun perform(uiController: UiController?, view: View?) {
    // do nothing as we just want to click again.
  }
}
