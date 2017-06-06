package com.jraska.github.client;

import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import okhttp3.HttpUrl;

final class ChromeCustomTabsLauncher implements WebLinkLauncher {
  private final TopActivityProvider provider;

  ChromeCustomTabsLauncher(TopActivityProvider provider) {
    this.provider = provider;
  }

  public void launch(HttpUrl url) {
    CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
    Uri uri = Uri.parse(url.toString());

    customTabsIntent.launchUrl(provider.top(), uri);
  }
}
