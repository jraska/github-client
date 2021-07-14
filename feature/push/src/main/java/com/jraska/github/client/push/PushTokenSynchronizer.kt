package com.jraska.github.client.push

import android.os.Build
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.installations.FirebaseInstallations
import com.jraska.github.client.time.DateTimeProvider
import timber.log.Timber
import java.util.HashMap
import javax.inject.Inject

internal class PushTokenSynchronizer @Inject constructor(
  private val database: FirebaseDatabase,
  private val dateTimeProvider: DateTimeProvider
) {

  fun synchronizeToken(token: String) {
    val installationIdTask = FirebaseInstallations.getInstance().id

    installationIdTask.addOnSuccessListener { saveToken(it, token) }
      .addOnFailureListener { Timber.e(it, "installation Id couldn't be found.") }
  }

  private fun saveToken(id: String, pushToken: String) {
    Timber.d("Id: %s, Token: %s", id, pushToken)

    val map = HashMap<String, Any>()
    map["date"] = dateTimeProvider.now().toString()

    map["push_token"] = pushToken
    map["android_os"] = Build.VERSION.RELEASE
    map["manufacturer"] = Build.BRAND
    map["model"] = Build.MODEL

    val tokenReference = database.getReference("devices/$id")
    tokenReference.setValue(map)
  }
}
