package com.jraska.github.client.xpush

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.RemoteMessage
import com.jraska.github.client.DecoratedServiceModelFactory
import com.jraska.github.client.DeepLinkLaunchTest
import com.jraska.github.client.TestUITestApp
import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.push.PushHandleModel
import org.junit.After
import org.junit.Assume
import org.junit.Before
import org.junit.Test

class PushIntegrationTest {

  lateinit var pushClient: PushServerClient
  lateinit var thisDeviceToken: String

  @Suppress("UNCHECKED_CAST") // We want to fail on unchecked casting
  @Before
  fun setUp() {
    pushClient = PushServerClient.create(apiKey())
    thisDeviceToken = FirebaseInstanceId.getInstance().token!!

    TestUITestApp.get().decoratedServiceFactory.decorator = object : DecoratedServiceModelFactory.Decorator {
      override fun <T : ServiceModel> create(modelClass: Class<T>, productionFactory: ServiceModel.Factory): T {
        return TestPushHandleModel(productionFactory.create(modelClass) as PushHandleModel) as T
      }
    }

    IdlingRegistry.getInstance().register(PushAwaitIdlingResource.idlingResource())
  }

  @After
  fun tearDown() {
    IdlingRegistry.getInstance().unregister(PushAwaitIdlingResource.idlingResource())
    TestUITestApp.get().decoratedServiceFactory.decorator = null
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
    PushAwaitIdlingResource.waitForPush()
  }

  object PushAwaitIdlingResource {
    private val countingIdlingResource = CountingIdlingResource("Push Await")

    fun idlingResource(): IdlingResource = countingIdlingResource

    fun waitForPush() = countingIdlingResource.increment()

    fun onPush() = countingIdlingResource.decrement()
  }

  class TestPushHandleModel(
    val productionModel: PushHandleModel,
  ) : PushHandleModel by productionModel {
    override fun onMessageReceived(message: RemoteMessage) {
      productionModel.onMessageReceived(message)
      PushAwaitIdlingResource.onPush()
    }
  }
}
