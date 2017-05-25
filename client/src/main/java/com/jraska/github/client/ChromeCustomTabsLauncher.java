package com.jraska.github.client;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

import okhttp3.HttpUrl;

final class ChromeCustomTabsLauncher implements WebLinkLauncher {
  private final Activity activity;

  ChromeCustomTabsLauncher(Activity activity) {
    this.activity = activity;
  }

  public void launch(HttpUrl url) {
    CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
    Uri uri = Uri.parse(url.toString());

    customTabsIntent.launchUrl(activity, uri);
  }
}
