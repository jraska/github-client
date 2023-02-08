package com.jraska.github.client.identity.google

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
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
    eventAnalytics.report(AnalyticsEvent.create(SIGN_IN_CLICKED))

    googleSignInRepository.triggerSignIn()
  }

  fun onLogoutButtonClick() {
    eventAnalytics.report(AnalyticsEvent.create(SIGN_OUT_CLICKED))

    googleSignInRepository.signOut()
  }

  fun onDisconnectButtonClick() {
    eventAnalytics.report(AnalyticsEvent.create(DISCONNECT_CLICKED))

    googleSignInRepository.revokeAccess()
  }

  fun onGoogleSignInResultReceived(data: Intent) {
    googleSignInRepository.onSignInIntent(data)
  }

  companion object {
    private val SIGN_IN_CLICKED = AnalyticsEvent.Key("google_sign_in_clicked", Owner.CORE_TEAM)
    private val SIGN_OUT_CLICKED = AnalyticsEvent.Key("google_sign_out_clicked", Owner.CORE_TEAM)
    private val DISCONNECT_CLICKED = AnalyticsEvent.Key("google_sign_in_clicked", Owner.CORE_TEAM)

    private val LOGGED_OUT = ViewState(
      signInButtonVisible = true,
      signOutButtonVisible = false,
      disconnectButtonVisible = false
    )
    private val LOGGED_IN = ViewState(
      signInButtonVisible = false,
      signOutButtonVisible = true,
      disconnectButtonVisible = true
    )
  }
}

class ViewState(
  val signInButtonVisible: Boolean,
  val signOutButtonVisible: Boolean,
  val disconnectButtonVisible: Boolean,
)


