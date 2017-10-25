package com.jraska.github.client.push

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import com.google.firebase.messaging.RemoteMessageBridge

class PushIntentObserver(private val pushHandler: PushHandler) : LifecycleObserver {

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreate(owner: LifecycleOwner) {
    if (owner !is Activity) {
      return
    }

    val activity = owner as Activity
    val intent = activity.intent
    if (isPush(intent)) {
      val message = RemoteMessageBridge.create(intent.extras!!)
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
}
