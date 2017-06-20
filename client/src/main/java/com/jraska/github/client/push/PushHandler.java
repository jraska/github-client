package com.jraska.github.client.push;

import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.EventAnalytics;
import timber.log.Timber;

import javax.inject.Inject;

public final class PushHandler {
  private final EventAnalytics eventAnalytics;

  @Inject PushHandler(EventAnalytics eventAnalytics) {
    this.eventAnalytics = eventAnalytics;
  }

  void handlePush(PushAction action) {
    Timber.v("Push received action: %s", action.name);

    AnalyticsEvent pushHandled = AnalyticsEvent.builder("push_handled")
      .addProperty("push_action", action.name)
      .build();
    eventAnalytics.report(pushHandled);
  }
}
