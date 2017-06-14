package com.jraska.github.client;

import okhttp3.HttpUrl;

/**
 * All navigation will be done through deep links,
 * even the internal one to avoid two different
 * ways how to handle activity start.
 */
final class DeepLinkNavigator implements Navigator {
  private final DeepLinkLauncher deepLinkLauncher;
  private final WebLinkLauncher webLinkLauncher;

  public DeepLinkNavigator(DeepLinkLauncher deepLinkLauncher, WebLinkLauncher webLinkLauncher) {
    this.deepLinkLauncher = deepLinkLauncher;
    this.webLinkLauncher = webLinkLauncher;
  }

  private void launch(HttpUrl url) {
    deepLinkLauncher.launch(url);
  }

  @Override public void launchOnWeb(HttpUrl httpUrl) {
    webLinkLauncher.launch(httpUrl);
  }

  @Override public void startUsersList() {
    HttpUrl url = HttpUrl.parse("https://github.com/users");
    launch(url);
  }

  @Override public void startUserDetail(String login) {
    HttpUrl url = Urls.user(login);
    launch(url);
  }

  @Override public void startRepoDetail(String fullPath) {
    HttpUrl url = Urls.repo(fullPath);
    launch(url);
  }
}
