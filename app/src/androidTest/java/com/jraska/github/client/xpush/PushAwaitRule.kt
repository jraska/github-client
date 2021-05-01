package com.jraska.github.client.xpush

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.google.firebase.messaging.RemoteMessage
import com.jraska.github.client.push.PushHandleModel
import org.junit.rules.ExternalResource

class PushAwaitRule : ExternalResource() {
  override fun before() {
    IdlingRegistry.getInstance().register(PushAwaitIdlingResource.idlingResource)
  }

  override fun after() {
    IdlingRegistry.getInstance().unregister(PushAwaitIdlingResource.idlingResource)
  }

  fun onViewWillAwaitPush() {
    PushAwaitIdlingResource.waitForPush()
  }

  private object PushAwaitIdlingResource {
    private val countingIdlingResource = CountingIdlingResource("Push Await")

    val idlingResource: IdlingResource = countingIdlingResource

    fun waitForPush() = countingIdlingResource.increment()

    fun onPush() = countingIdlingResource.decrement()
  }

  class TestPushHandleModel(
    private val productionModel: PushHandleModel,
  ) : PushHandleModel by productionModel {
    override fun onMessageReceived(message: RemoteMessage) {
      productionModel.onMessageReceived(message)
      PushAwaitIdlingResource.onPush()
    }
  }
}
