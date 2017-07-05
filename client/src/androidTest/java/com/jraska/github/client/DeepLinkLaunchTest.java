package com.jraska.github.client;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;

import org.hamcrest.Matchers;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class DeepLinkLaunchTest {
  @Test
  public void whenUsersLink_thenUsersActivityDisplayed() {
    launchDeepLink("https://github.com/users");

    onView(withText("defunkt")).check(matches(isDisplayed()));
    onView(withText("mojombo")).check(matches(isDisplayed()));
  }

  @Test
  public void whenDetailLink_thenUserDetailActivityDisplayed() {
    launchDeepLink("https://github.com/defunkt");

    onView(withText("dotjs")).check(matches(isDisplayed()));
  }

  @Test
  public void whenRepoLink_thenRepoActivityDisplayed() {
    launchDeepLink("https://github.com/jraska/Falcon");

    onView(withText(Matchers.containsString("Language: Java")))
      .check(matches(isDisplayed()));
  }

  public static void launchDeepLink(String deepLink) {
    Context targetContext = InstrumentationRegistry.getTargetContext();

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setPackage(targetContext.getPackageName());
    intent.setData(Uri.parse(deepLink));
    targetContext.startActivity(intent);
  }
}
