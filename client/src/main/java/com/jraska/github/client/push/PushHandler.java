package com.jraska.github.client.push;

import com.jraska.github.client.Config;
import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.EventAnalytics;

import javax.inject.Inject;

import dagger.Lazy;
import timber.log.Timber;

public final class PushHandler {
  private static final String ACTION_REFRESH_CONFIG = "refresh_config";

  private final EventAnalytics eventAnalytics;
  private final TokenSynchronizer tokenSynchronizer;
  private final Lazy<Config> config;

  @Inject PushHandler(EventAnalytics eventAnalytics, TokenSynchronizer tokenSynchronizer, Lazy<Config> config) {
    this.eventAnalytics = eventAnalytics;
    this.tokenSynchronizer = tokenSynchronizer;
    this.config = config;
  }

  void handlePush(PushAction action) {
    Timber.v("Push received action: %s", action.name);

    boolean handled = handleInternal(action);

    if (handled) {
      AnalyticsEvent pushHandled = AnalyticsEvent.builder("push_handled")
        .addProperty("push_action", action.name)
        .build();
      eventAnalytics.report(pushHandled);
    } else {
      AnalyticsEvent pushHandled = AnalyticsEvent.builder("push_not_handled")
        .addProperty("push_action", action.name)
        .build();
      eventAnalytics.report(pushHandled);
    }
  }

  boolean handleInternal(PushAction action) {
    switch (action.name) {
      case ACTION_REFRESH_CONFIG:
        config.get().triggerRefresh();
        return true;

      default:
        return false;
    }
  }

  void onTokenRefresh() {
    tokenSynchronizer.synchronizeToken();

    AnalyticsEvent tokenEvent = AnalyticsEvent.create("push_token_refresh");
    eventAnalytics.report(tokenEvent);
  }
}
