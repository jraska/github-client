package com.jraska.github.client.xpush

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.google.firebase.messaging.RemoteMessage
import com.jraska.github.client.DecoratedServiceModelFactory
import com.jraska.github.client.TestUITestApp
import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.push.PushHandleModel
import org.junit.rules.ExternalResource

class PushAwaitRule : ExternalResource() {
  override fun before() {
    TestUITestApp.get().decoratedServiceFactory.decorator = object : DecoratedServiceModelFactory.Decorator {
      override fun <T : ServiceModel> create(modelClass: Class<T>, productionFactory: ServiceModel.Factory): T {
        @Suppress("UNCHECKED_CAST") // We want to fail if someoen creates other models during the test
        return TestPushHandleModel(productionFactory.create(modelClass) as PushHandleModel) as T
      }
    }

    IdlingRegistry.getInstance().register(PushAwaitIdlingResource.idlingResource())
  }

  override fun after() {
    IdlingRegistry.getInstance().unregister(PushAwaitIdlingResource.idlingResource())
    TestUITestApp.get().decoratedServiceFactory.decorator = null
  }

  fun waitForPush() {
    PushAwaitIdlingResource.waitForPush()
  }

  private object PushAwaitIdlingResource {
    private val countingIdlingResource = CountingIdlingResource("Push Await")

    fun idlingResource(): IdlingResource = countingIdlingResource

    fun waitForPush() = countingIdlingResource.increment()

    fun onPush() = countingIdlingResource.decrement()
  }

  private class TestPushHandleModel(
    val productionModel: PushHandleModel,
  ) : PushHandleModel by productionModel {
    override fun onMessageReceived(message: RemoteMessage) {
      productionModel.onMessageReceived(message)
      PushAwaitIdlingResource.onPush()
    }
  }
}
