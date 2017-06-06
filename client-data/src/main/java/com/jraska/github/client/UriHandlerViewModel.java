package com.jraska.github.client;

import android.arch.lifecycle.ViewModel;
import okhttp3.HttpUrl;

public final class UriHandlerViewModel extends ViewModel {
  private final DeepLinkHandler deepLinkHandler;

  UriHandlerViewModel(DeepLinkHandler deepLinkHandler) {
    this.deepLinkHandler = deepLinkHandler;
  }

  public void handleDeepLink(String deepLinkText) {
    HttpUrl deepLink = HttpUrl.parse(deepLinkText);
    deepLinkHandler.handleDeepLink(deepLink);
  }
}
