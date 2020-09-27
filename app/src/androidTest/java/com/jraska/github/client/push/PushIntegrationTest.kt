package com.jraska.github.client.push

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.iid.FirebaseInstanceId
import com.jraska.github.client.DeepLinkLaunchTest
import org.junit.Assume
import org.junit.Before
import org.junit.Test

class PushIntegrationTest {

  lateinit var pushClient: PushServerClient
  lateinit var thisDeviceToken: String

  @Before
  fun setUp() {
    pushClient = PushServerClient.create(apiKey())
    thisDeviceToken = FirebaseInstanceId.getInstance().token!!
  }

  @Test
  fun testPushIntegration_fromSettingsToAbout() {
    DeepLinkLaunchTest.launchDeepLink("https://github.com/settings")

    sendDeepLinKMessage("https://github.com/about")

    awaitPush()
    onView(withText("by Josef Raska")).check(matches(isDisplayed()))
  }

  @Test
  fun testPushIntegration_fromAboutToSettings() {
    DeepLinkLaunchTest.launchDeepLink("https://github.com/about")

    sendDeepLinKMessage("https://github.com/settings")

    awaitPush()
    onView(withText("Purchase")).check(matches(isDisplayed()))
  }

  private fun sendDeepLinKMessage(deepLink: String) {
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
    // TODO: 27/09/2020 Idling resource on Push
    Thread.sleep(3_000)
  }
}
