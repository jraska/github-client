package com.jraska.github.client.users;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jraska.github.client.ui.UsersActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UsersActivityFlowTest {
  @Rule
  public ActivityTestRule<UsersActivity> activityRule = new ActivityTestRule<>(UsersActivity.class);

  @Test
  public void whenStarts_thenDisplaysUsers() throws InterruptedException {
    activityRule.getActivity();

    onView(withText("defunkt")).perform(click());
    onView(withText("dotjs")).check(matches(isDisplayed()));
  }
}
