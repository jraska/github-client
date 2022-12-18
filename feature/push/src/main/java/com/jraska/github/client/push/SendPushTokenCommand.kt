package com.jraska.github.client.push

import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber
import javax.inject.Inject

internal class SendPushTokenCommand @Inject constructor(
  private val synchronizer: PushTokenSynchronizer
) : PushActionCommand {
  override fun execute(action: PushAction): PushExecuteResult {
    FirebaseMessaging.getInstance().token.addOnSuccessListener {
      synchronizer.synchronizeToken(it)
    }.addOnFailureListener { Timber.e(it, "Error getting push token to send") }

    return PushExecuteResult.SUCCESS
  }
}
