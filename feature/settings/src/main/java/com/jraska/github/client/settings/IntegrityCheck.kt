package com.jraska.github.client.settings

import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.jraska.github.client.coroutines.AppDispatchers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class IntegrityCheck @Inject constructor(
  private val appDispatchers: AppDispatchers,
  private val context: Context
) {
  @OptIn(DelicateCoroutinesApi::class)
  fun run() {
    GlobalScope.launch(appDispatchers.io) {
      runInternal()
    }
  }

  private fun runInternal() {
    val integrityTokenRequest = IntegrityTokenRequest.builder()
      .setNonce(UUID.randomUUID().toString())
      .build()

    val requestIntegrityTokenTask = IntegrityManagerFactory.create(context)
      .requestIntegrityToken(integrityTokenRequest)

    try {
      val token = Tasks.await(requestIntegrityTokenTask)
      Timber.d("Integrity result %s", token)
    } catch (exception: Exception) {
      Timber.e(exception, "Error checking integrity")
    }
  }
}
