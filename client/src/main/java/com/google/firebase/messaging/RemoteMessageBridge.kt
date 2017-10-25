package com.google.firebase.messaging

import android.os.Bundle

/**
 * I believe this is better then copying internal implementations of it.
 */
object RemoteMessageBridge {
  fun create(bundle: Bundle): RemoteMessage {
    return RemoteMessage(bundle)
  }
}
