package com.jraska.github.client;

import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import com.jraska.github.client.ui.BaseActivity;
import okhttp3.HttpUrl;

import javax.inject.Provider;

final class ChromeCustomTabsLauncher implements WebLinkLauncher {
  private final Provider<BaseActivity> provider;

  ChromeCustomTabsLauncher(Provider<BaseActivity> provider) {
    this.provider = provider;
  }

  public void launch(HttpUrl url) {
    CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
    Uri uri = Uri.parse(url.toString());

    customTabsIntent.launchUrl(provider.get(), uri);
  }
}
