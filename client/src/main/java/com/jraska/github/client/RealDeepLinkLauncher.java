package com.jraska.github.client;

import com.jraska.github.client.ui.BaseActivity;
import com.jraska.github.client.ui.UserDetailActivity;
import com.jraska.github.client.ui.UsersActivity;
import okhttp3.HttpUrl;

import javax.inject.Provider;

final class RealDeepLinkLauncher implements DeepLinkLauncher {
  private final Provider<BaseActivity> topActivityProvider;

  RealDeepLinkLauncher(Provider<BaseActivity> topActivityProvider) {
    this.topActivityProvider = topActivityProvider;
  }

  @Override public void launch(HttpUrl deepLink) {
    if (!deepLink.host().equals("github.com")) {
      throw new IllegalArgumentException("We handle only GitHub deep links, not: " + deepLink);
    }

    if ("/users".equals(deepLink.encodedPath())) {
      UsersActivity.start(topActivityProvider.get());
      return;
    }

    if (deepLink.pathSize() == 1) {
      String login = deepLink.pathSegments().get(0);
      UserDetailActivity.start(topActivityProvider.get(), login);
      return;
    }

    throw new IllegalArgumentException("Unexpected deep link: " + deepLink);
  }
}
