package com.jraska.github.client;

import okhttp3.HttpUrl;

public interface Navigator {
  void launchOnWeb(HttpUrl httpUrl);

  void startUsersList();

  void startUserDetail(String login);

  void startRepoDetail(String fullPath);
}
