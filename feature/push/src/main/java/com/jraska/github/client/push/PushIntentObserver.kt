package com.jraska.github.client.push

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.google.firebase.messaging.RemoteMessage
import com.jraska.github.client.core.android.DefaultActivityCallbacks
import com.jraska.github.client.core.android.OnAppCreate
import javax.inject.Inject

internal class PushIntentObserver(private val pushHandler: PushHandler) {
  fun onCreate(activity: Activity) {
    val intent = activity.intent
    if (isPush(intent)) {
      val message = RemoteMessage(intent.extras!!)
      val action = RemoteMessageToActionConverter.convert(message)

      pushHandler.handlePush(action)
    }
  }

  private fun isPush(intent: Intent): Boolean {
    if (Intent.ACTION_MAIN != intent.action || intent.extras == null) {
      return false
    }

    for (key in intent.extras!!.keySet()) {
      if (key.startsWith("google.sent_time")) {
        return true
      }

      if (key.startsWith("google.to")) {
        return true
      }

      if (key.startsWith("gcm.")) {
        return true
      }
    }

    return false
  }

  class CallbacksSetup @Inject constructor(
    private val pushHandler: PushHandler
  ) : OnAppCreate {

    override fun onCreate(app: Application) {
      app.registerActivityLifecycleCallbacks(PushCallbacks(PushIntentObserver(pushHandler)))
    }
  }

  internal class PushCallbacks(private val intentObserver: PushIntentObserver) : DefaultActivityCallbacks() {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
      intentObserver.onCreate(activity)
    }
  }
}
