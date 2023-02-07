package com.jraska.github.client.identity.google

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jraska.github.client.analytics.EventAnalytics
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GoogleSignInViewModel @Inject constructor(
  private val eventAnalytics: EventAnalytics,
  private val googleSignInRepository: GoogleSignInRepository,
) : ViewModel() {
  private val stateLiveData: LiveData<ViewState> by lazy {
    googleSignInRepository.status()
      .map { if (it.loggedIn) LOGGED_IN else LOGGED_OUT }
      .asLiveData()
  }

  fun viewState(): LiveData<ViewState> {
    return stateLiveData
  }

  fun onLoginButtonClick() {
    googleSignInRepository.triggerSignIn()
  }

  fun onLogoutButtonClick() {
    googleSignInRepository.signOut()
  }

  fun onDisconnectButtonClick() {
    googleSignInRepository.revokeAccess()
  }

  fun onGoogleSignInResultReceived(data: Intent) {
    googleSignInRepository.onSignInIntent(data)
  }
}

class ViewState(
  val signInButtonVisible: Boolean,
  val signOutButtonVisible: Boolean,
  val disconnectButtonVisible: Boolean,
)

val LOGGED_OUT = ViewState(
  signInButtonVisible = true,
  signOutButtonVisible = false,
  disconnectButtonVisible = false
)
val LOGGED_IN = ViewState(
  signInButtonVisible = false,
  signOutButtonVisible = true,
  disconnectButtonVisible = true
)
