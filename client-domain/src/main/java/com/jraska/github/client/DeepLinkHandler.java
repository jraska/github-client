package com.jraska.github.client;

import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.EventReporter;
import com.jraska.github.client.logging.CrashReporter;

import javax.inject.Inject;

import okhttp3.HttpUrl;

public final class DeepLinkHandler {
  private final DeepLinkLauncher linkLauncher;
  private final WebLinkLauncher fallbackLauncher;
  private final CrashReporter crashReporter;
  private final EventReporter eventReporter;

  @Inject
  public DeepLinkHandler(DeepLinkLauncher linkLauncher, WebLinkLauncher fallbackLauncher,
                         CrashReporter crashReporter, EventReporter eventReporter) {
    this.linkLauncher = linkLauncher;
    this.fallbackLauncher = fallbackLauncher;
    this.crashReporter = crashReporter;
    this.eventReporter = eventReporter;
  }

  public void handleDeepLink(HttpUrl deepLink) {
    AnalyticsEvent event = AnalyticsEvent.builder("deep_link_received")
      .addProperty("deep_link", deepLink.toString())
      .build();
    eventReporter.report(event);

    boolean launch = linkLauncher.launch(deepLink);
    if (!launch) {
      crashReporter.log("Unhandled deep link: " + deepLink);
      fallbackLauncher.launch(deepLink);
    }
  }
}
