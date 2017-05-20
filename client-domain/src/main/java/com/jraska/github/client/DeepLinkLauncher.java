package com.jraska.github.client;

import okhttp3.HttpUrl;

public interface DeepLinkLauncher {
  boolean launch(HttpUrl deepLink);
}
