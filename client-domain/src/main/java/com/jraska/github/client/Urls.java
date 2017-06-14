package com.jraska.github.client;

import com.jraska.github.client.common.Preconditions;

import okhttp3.HttpUrl;

public final class Urls {
  public static HttpUrl user(String login) {
    return HttpUrl.parse("https://github.com/" + login);
  }

  public static HttpUrl repo(String fullPath) {
    return HttpUrl.parse("https://github.com/" + fullPath);
  }

  private Urls() {
    Preconditions.throwNoInstances();
  }
}
