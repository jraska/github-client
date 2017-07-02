package com.jraska.github.client.push;

import com.jraska.github.client.Config;
import com.jraska.github.client.analytics.AnalyticsEvent;
import com.jraska.github.client.analytics.AnalyticsProperty;
import com.jraska.github.client.analytics.EventAnalytics;

import javax.inject.Inject;

import dagger.Lazy;
import timber.log.Timber;

public final class PushHandler {
  private static final String ACTION_REFRESH_CONFIG = "refresh_config";
  private static final String ACTION_CONFIG_VALUE_AS_PROPERTY = "set_config_as_property";
  private static final String ACTION_SET_ANALYTICS_PROPERTY = "set_analytics_property";

  private final EventAnalytics eventAnalytics;
  private final PushTokenSynchronizer tokenSynchronizer;
  private final Lazy<Config> config;
  private final Lazy<AnalyticsProperty> analyticsProperty;

  @Inject PushHandler(EventAnalytics eventAnalytics, PushTokenSynchronizer pushTokenSynchronizer,
                      Lazy<Config> config, Lazy<AnalyticsProperty> analyticsProperty) {
    this.eventAnalytics = eventAnalytics;
    this.tokenSynchronizer = pushTokenSynchronizer;
    this.config = config;
    this.analyticsProperty = analyticsProperty;
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
        return refreshConfig();
      case ACTION_CONFIG_VALUE_AS_PROPERTY:
        return configAsProperty(action);
      case ACTION_SET_ANALYTICS_PROPERTY:
        return setAnalyticsProperty(action);

      default:
        return false;
    }
  }

  private boolean setAnalyticsProperty(PushAction action) {
    String key = action.parameters.get("property_key");
    if (key == null) {
      return false;
    }

    String value = action.parameters.get("property_value");
    if (value == null) {
      return false;
    }

    analyticsProperty.get().setUserProperty(key, value);
    return true;
  }

  private boolean configAsProperty(PushAction action) {
    String key = action.parameters.get("config_key");
    if (key == null) {
      return false;
    }

    String value = config.get().getString(key);
    analyticsProperty.get().setUserProperty(key, value);
    return true;
  }

  private boolean refreshConfig() {
    config.get().triggerRefresh();
    return true;
  }

  public void onTokenRefresh() {
    tokenSynchronizer.synchronizeToken();

    AnalyticsEvent tokenEvent = AnalyticsEvent.create("push_token_refresh");
    eventAnalytics.report(tokenEvent);
  }
}
