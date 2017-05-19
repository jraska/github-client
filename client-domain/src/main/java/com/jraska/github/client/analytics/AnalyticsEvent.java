package com.jraska.github.client.analytics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class AnalyticsEvent {
  final String name;
  final Map<String, String> properties;

  private AnalyticsEvent(String name, Map<String, String> properties) {
    this.name = name;
    this.properties = properties;
  }

  public static AnalyticsEvent create(String name) {
    return new AnalyticsEvent(name, Collections.emptyMap());
  }

  public static Builder builder(String name) {
    return new Builder(name);
  }

  public static class Builder {
    private final String name;
    private final Map<String, String> properties = new HashMap<>();

    Builder(String name) {
      this.name = name;
    }

    public Builder addProperty(String name, String value) {
      properties.put(name, value);
      return this;
    }

    public AnalyticsEvent build() {
      return new AnalyticsEvent(name, Collections.unmodifiableMap(properties));
    }
  }
}
