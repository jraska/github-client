package com.jraska.github.client.push

import android.os.Build
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.jraska.github.client.time.DateTimeProvider
import java.util.HashMap
import javax.inject.Inject
import timber.log.Timber

internal class PushTokenSynchronizer @Inject constructor(
  private val database: FirebaseDatabase,
  private val dateTimeProvider: DateTimeProvider
) {

  fun onTokenRefresh(token: String) {
    val instanceId = FirebaseInstanceId.getInstance()
    val id = instanceId.id

    Timber.d("Id: %s, Token: %s", id, token)

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
