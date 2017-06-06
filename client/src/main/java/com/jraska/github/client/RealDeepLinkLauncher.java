package com.jraska.github.client;

import com.jraska.github.client.ui.UserDetailActivity;
import com.jraska.github.client.ui.UsersActivity;
import okhttp3.HttpUrl;

final class RealDeepLinkLauncher implements DeepLinkLauncher {
  private final TopActivityProvider topActivityProvider;

  RealDeepLinkLauncher(TopActivityProvider topActivityProvider) {
    this.topActivityProvider = topActivityProvider;
  }

  @Override public void launch(HttpUrl deepLink) {
    if (!deepLink.host().equals("github.com")) {
      throw new IllegalArgumentException("We handle only GitHub deep links, not: " + deepLink);
    }

    if ("/users".equals(deepLink.encodedPath())) {
      UsersActivity.start(topActivityProvider.top());
      return;
    }

    if (deepLink.pathSize() == 1) {
      String login = deepLink.pathSegments().get(0);
      UserDetailActivity.start(topActivityProvider.top(), login);
      return;
    }

    throw new IllegalArgumentException("Unexpected deep link: " + deepLink);
  }
}
