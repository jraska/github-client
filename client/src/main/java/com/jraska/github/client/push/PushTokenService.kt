package com.jraska.github.client.push

import com.google.firebase.iid.FirebaseInstanceIdService
import com.jraska.github.client.GitHubClientApp

class PushTokenService : FirebaseInstanceIdService() {
  override fun onTokenRefresh() {
    val app = application as GitHubClientApp
    app.pushHandler().onTokenRefresh()
  }
}
