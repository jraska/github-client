package com.jraska.github.client;

import android.app.Activity;

import com.jraska.github.client.ui.UserDetailActivity;
import com.jraska.github.client.ui.UsersActivity;

import okhttp3.HttpUrl;

final class RealDeepLinkLauncher implements DeepLinkLauncher {
  private final Activity activity;

  RealDeepLinkLauncher(Activity activity) {
    this.activity = activity;
  }

  @Override public boolean launch(HttpUrl deepLink) {
    if (!deepLink.host().equals("github.com")) {
      throw new IllegalArgumentException("We handle only GitHub deep links");
    }

    if ("/users".equals(deepLink.encodedPath())) {
      UsersActivity.start(activity);
      return true;
    }

    if (deepLink.pathSize() == 1) {
      String login = deepLink.pathSegments().get(0);
      UserDetailActivity.start(activity, login);
      return true;
    }

    return false;
  }
}
