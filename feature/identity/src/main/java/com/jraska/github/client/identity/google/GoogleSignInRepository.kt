package com.jraska.github.client.identity.google

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.jraska.github.client.core.android.TopActivityProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class GoogleSignInRepository @Inject constructor(
  private val context: Context,
  private val topActivityProvider: TopActivityProvider
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
    } else {
      signInStatus.tryEmit(toStatus(null))
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
  }
}
