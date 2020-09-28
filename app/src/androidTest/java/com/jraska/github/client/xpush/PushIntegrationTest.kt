package com.jraska.github.client.xpush

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.tasks.Tasks
import com.google.firebase.iid.FirebaseInstanceId
import com.jraska.github.client.DeepLinkLaunchTest
import org.junit.Assume
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PushIntegrationTest {

  lateinit var pushClient: PushServerClient
  lateinit var thisDeviceToken: String

  @get:Rule
  val pushRule = PushAwaitRule()

  @Before
  fun setUp() {
    pushClient = PushServerClient.create(apiKey())

    val instanceIdTask = FirebaseInstanceId.getInstance().instanceId
    thisDeviceToken = Tasks.await(instanceIdTask).token
  }

  @Test
  fun testPushIntegration_fromSettingsToAbout() {
    DeepLinkLaunchTest.launchDeepLink("https://github.com/settings")

    sendDeepLinkPush("https://github.com/about")

    awaitPush()
    onView(withText("by Josef Raska")).check(matches(isDisplayed()))
  }

  @Test
  fun testPushIntegration_fromAboutToSettings() {
    DeepLinkLaunchTest.launchDeepLink("https://github.com/about")

    sendDeepLinkPush("https://github.com/settings")

    awaitPush()
    onView(withText("Purchase")).check(matches(isDisplayed()))
  }

  // See LaunchDeepLinkCommand to see how this is handled.
  private fun sendDeepLinkPush(deepLink: String) {
    val messageToThisDevice = PushServerDto().apply {
      ids.add(thisDeviceToken)
      data["action"] = "launch_deep_link"
      data["deepLink"] = deepLink
    }

    pushClient.sendPush(messageToThisDevice).blockingAwait()
  }

  private fun apiKey(): String {
    val apiKey = InstrumentationRegistry.getArguments()["FCM_API_KEY"]
    Assume.assumeTrue("FCM key not found in argument 'FCM_API_KEY', ignoring the test.", apiKey is String)

    return apiKey as String
  }

  private fun awaitPush() {
    pushRule.waitForPush()
  }
}
