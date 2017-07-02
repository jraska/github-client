package com.jraska.github.client.push;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jraska.github.client.GitHubClientApp;

public final class PushTokenService extends FirebaseInstanceIdService {
  @Override public void onTokenRefresh() {
    GitHubClientApp app = ((GitHubClientApp) getApplication());
    app.pushHandler().onTokenRefresh();
  }
}
