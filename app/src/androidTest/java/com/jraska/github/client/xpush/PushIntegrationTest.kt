package com.jraska.github.client.xpush

import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.jraska.github.client.DeepLinkLaunchTest
import com.jraska.github.client.EnableConfigRule
import com.jraska.github.client.FakeCoreModule
import com.jraska.github.client.TestUITestApp
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assume
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PushIntegrationTest {

  lateinit var pushClient: PushServerClient
  lateinit var thisDeviceToken: String

  @get:Rule
  val pushRule = PushAwaitRule()

  @get:Rule
  val enableConfigRule = EnableConfigRule()

  @Before
  fun setUp() {
    pushClient = PushServerClient.create(apiKey())

    val tokenTask = FirebaseMessaging.getInstance().token
    thisDeviceToken = Tasks.await(tokenTask)
  }

  @Test
  fun testPushIntegrationFromSettingsToAbout() {
    DeepLinkLaunchTest.launchDeepLink("https://github.com/settings")

    sendDeepLinkPush("https://github.com/about")

    onView(withText("by Josef Raska")).check(matches(isDisplayed()))
  }

  @Test
  fun testPushIntegrationMultipleNotifications() {
    DeepLinkLaunchTest.launchDeepLink("https://github.com/about")

    sendDeepLinkPush("https://github.com/settings")
    onView(withText("Purchase")).check(matches(isDisplayed()))

    sendPush(mapOf("action" to "press_back"))
    onView(withText("by Josef Raska")).check(matches(isDisplayed()))

    fakeConfig().resetRefreshTracking()
    sendPush(mapOf("action" to "refresh_config"))
    assertThat(fakeConfig().configRefreshTriggered).isTrue

    enableConfigRule.set("pushConfig" to "HeyHou")
    sendPush(mapOf("action" to "set_config_as_property", "config_key" to "pushConfig"))
    assertThat(FakeCoreModule.analyticProperty.properties()).containsEntry("pushConfig", "HeyHou")

    sendPush(
      mapOf(
        "action" to "set_analytics_property",
        "property_key" to "testProperty",
        "property_value" to "testPropertyValue"
      )
    )
    assertThat(FakeCoreModule.analyticProperty.properties()).containsEntry("testProperty", "testPropertyValue")
  }

  private fun fakeConfig() = TestUITestApp.get().appComponent.config

  private fun sendDeepLinkPush(deepLink: String) {
    sendPush(
      mapOf(
        "action" to "launch_deep_link",
        "deepLink" to deepLink
      )
    )
  }

  private fun sendPush(dataMap: Map<String, String>) {
    pushRule.onViewAwaitPush()

    val messageToThisDevice = PushServerDto().apply {
      ids.add(thisDeviceToken)
      data.putAll(dataMap)
    }

    pushClient.sendPush(messageToThisDevice).execute()

    onIdle()
  }

  private fun apiKey(): String {
    val apiKey = InstrumentationRegistry.getArguments()["FCM_API_KEY"]
    Assume.assumeTrue(
      "FCM key not found in argument 'FCM_API_KEY', ignoring the test.",
      apiKey is String
    )

    return apiKey as String
  }
}
