package com.jraska.github.client.ui;

import android.arch.lifecycle.ViewModel;

import com.jraska.github.client.DeepLinkLauncher;
import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.EventAnalytics;

import okhttp3.HttpUrl;

public final class ShortcutHandlerModel extends ViewModel {
  private final DeepLinkLauncher deepLinkLauncher;
  private final EventAnalytics eventAnalytics;

  public ShortcutHandlerModel(DeepLinkLauncher deepLinkLauncher, EventAnalytics eventAnalytics) {
    this.deepLinkLauncher = deepLinkLauncher;
    this.eventAnalytics = eventAnalytics;
  }

  void handleDeepLink(HttpUrl url) {
    AnalyticsEvent event = AnalyticsEvent.builder("shortcut_clicked")
      .addProperty("shortcut", url.toString())
      .build();

    eventAnalytics.report(event);

    deepLinkLauncher.launch(url);
  }
}
