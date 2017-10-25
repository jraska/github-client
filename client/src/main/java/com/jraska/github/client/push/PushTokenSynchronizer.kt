package com.jraska.github.client.push

import android.os.Build
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.jraska.github.client.time.DateTimeProvider
import timber.log.Timber
import java.util.*
import javax.inject.Inject

internal class PushTokenSynchronizer @Inject constructor(
  private val database: FirebaseDatabase,
  private val dateTimeProvider: DateTimeProvider) {

  fun synchronizeToken() {
    val instanceId = FirebaseInstanceId.getInstance()
    val id = instanceId.id
    val token = instanceId.token

    Timber.d("Id: %s, Token: %s", id, token)

    if (id == null || token == null) {
      return
    }

    val map = HashMap<String, Any>()
    map.put("date", dateTimeProvider.now().toString())

    map.put("push_token", token)
    map.put("android_os", Build.VERSION.RELEASE)
    map.put("manufacturer", Build.BRAND)
    map.put("model", Build.MODEL)

    val tokenReference = database.getReference("devices/" + id)
    tokenReference.setValue(map)
  }
}
