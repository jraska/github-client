package com.jraska.github.client.push

import android.os.Build
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.jraska.github.client.time.DateTimeProvider
import timber.log.Timber
import java.util.HashMap
import javax.inject.Inject

internal class PushTokenSynchronizer @Inject constructor(
  private val database: FirebaseDatabase,
  private val dateTimeProvider: DateTimeProvider
) {

  fun synchronizeToken() {
    val instanceId = FirebaseInstanceId.getInstance()
    val id = instanceId.id
    val token = instanceId.token

    Timber.d("Id: %s, Token: %s", id, token)

    if (id == null || token == null) {
      return
    }

    val map = HashMap<String, Any>()
    map["date"] = dateTimeProvider.now().toString()

    map["push_token"] = token
    map["android_os"] = Build.VERSION.RELEASE
    map["manufacturer"] = Build.BRAND
    map["model"] = Build.MODEL

    val tokenReference = database.getReference("devices/$id")
    tokenReference.setValue(map)
  }
}
