package com.jraska.github.client.identity.google

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.core.android.TopActivityProvider
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class GoogleSignInRepository @Inject constructor(
  private val context: Context,
  private val topActivityProvider: TopActivityProvider,
  private val eventAnalytics: EventAnalytics
) {

  private val signInStatus: MutableSharedFlow<GoogleSignInStatus> by lazy {
    signInStatus()
  }

  private val googleClient: GoogleSignInClient by lazy {
    GoogleSignIn.getClient(context, options())
  }

  fun status(): Flow<GoogleSignInStatus> {
    return signInStatus
  }

  fun revokeAccess() {
    googleClient.revokeAccess().addOnCompleteListener {
      signInStatus.tryEmit(currentStatus())
    }
  }

  fun signOut() {
    googleClient.signOut().addOnCompleteListener {
      signInStatus.tryEmit(currentStatus())
    }
  }

  private fun signInStatus(): MutableSharedFlow<GoogleSignInStatus> {
    val stateFlow = MutableSharedFlow<GoogleSignInStatus>(1)

    val toStatus = currentStatus()
    stateFlow.tryEmit(toStatus)

    return stateFlow
  }

  private fun currentStatus() = toStatus(GoogleSignIn.getLastSignedInAccount(context))

  fun triggerSignIn() {
    topActivityProvider.onTopActivity { activity ->
      (activity as FragmentActivity).supportFragmentManager
        .fragments
        .first { it is GoogleSignInFragment }
        .startActivityForResult(googleClient.signInIntent, REQUEST_CODE_SIGN_IN)
    }
  }

  fun onSignInIntent(data: Intent) {
    val signedInTask = GoogleSignIn.getSignedInAccountFromIntent(data)
    if (signedInTask.isSuccessful) {
      signInStatus.tryEmit(toStatus(signedInTask.result))

      eventAnalytics.report(AnalyticsEvent.create(SIGN_IN_SUCCESS))
    } else {
      val theError = errorMessage(signedInTask)
      val errorEvent = AnalyticsEvent.builder(SIGN_IN_FAILURE)
        .addProperty("error", theError)
        .build()

      eventAnalytics.report(errorEvent)

      signInStatus.tryEmit(toStatus(null))
    }
  }

  private fun errorMessage(task: Task<*>): String {
    val exception = task.exception ?: return "unknown - null exception"
    if(exception is ApiException) {
      return "Status code: ${exception.statusCode}"
    } else {
      Timber.e("Unexpected type of error %s", exception.javaClass.name)
      return "Unknown type"
    }
  }

  private fun options(): GoogleSignInOptions {
    return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestEmail()
      .build()
  }

  private fun toStatus(googleSignInAccount: GoogleSignInAccount?): GoogleSignInStatus {
    if (googleSignInAccount == null) {
      return GoogleSignInStatus(false)
    } else {
      return GoogleSignInStatus(true)
    }
  }

  class GoogleSignInStatus(
    val loggedIn: Boolean
  )

  companion object {
    const val REQUEST_CODE_SIGN_IN = 1

    private val SIGN_IN_SUCCESS = AnalyticsEvent.Key("google_sign_in_success", Owner.CORE_TEAM)
    private val SIGN_IN_FAILURE = AnalyticsEvent.Key("google_sign_in_failure", Owner.CORE_TEAM)
  }
}
