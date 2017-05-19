package com.jraska.github.client.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jraska.github.client.common.Preconditions;

import java.util.Map;

final class FirebaseEventReporter implements EventReporter {
  private final FirebaseAnalytics analytics;

  FirebaseEventReporter(FirebaseAnalytics analytics) {
    this.analytics = Preconditions.argNotNull(analytics);
  }

  @Override public void report(AnalyticsEvent event) {
    Bundle parameters = propertiesBundle(event.properties);
    analytics.logEvent(event.name, parameters);
  }

  private Bundle propertiesBundle(Map<String, String> properties) {
    if (properties.isEmpty()) {
      return null;
    }

    Bundle parameters = new Bundle();
    for (Map.Entry<String, String> analytic : properties.entrySet()) {
      parameters.putString(analytic.getKey(), analytic.getValue());
    }

    return parameters;
  }
}
