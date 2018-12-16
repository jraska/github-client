package com.jraska.github.client

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.jraska.github.client.http.ReplayHttpComponent
import okreplay.OkReplay
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

class DeepLinkLaunchTest {
  @get:Rule
  val testRule = ReplayHttpComponent.okReplayRule()

  @Test
  @OkReplay
  fun whenUsersLink_thenUsersActivityDisplayed() {
    launchDeepLink("https://github.com/users")

    onView(withText("defunkt")).check(matches(isDisplayed()))
    onView(withText("mojombo")).check(matches(isDisplayed()))
  }

  @Test
  @OkReplay
  fun whenDetailLink_thenUserDetailActivityDisplayed() {
    launchDeepLink("https://github.com/defunkt")

    onView(withText("dotjs")).check(matches(isDisplayed()))
  }

  @Test
  @OkReplay
  fun whenRepoLink_thenRepoActivityDisplayed() {
    launchDeepLink("https://github.com/jraska/Falcon")

    onView(withText(Matchers.containsString("Language: Java")))
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
