package com.jraska.github.client.push

import com.google.firebase.messaging.FirebaseMessaging
import com.jraska.github.client.common.BooleanResult
import timber.log.Timber
import javax.inject.Inject

internal class SendPushTokenCommand @Inject constructor(
  private val synchronizer: PushTokenSynchronizer
) : PushActionCommand {
  override fun execute(action: PushAction): BooleanResult {
    FirebaseMessaging.getInstance().token.addOnSuccessListener {
      synchronizer.synchronizeToken(it)
    }.addOnFailureListener { Timber.e(it, "Error getting push token to send") }

    return BooleanResult.SUCCESS
  }
}
