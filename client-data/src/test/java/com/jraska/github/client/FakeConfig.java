package com.jraska.github.client;

import com.jraska.github.client.common.Maps;

import java.util.Collections;
import java.util.Map;

public final class FakeConfig implements Config {
  private final Map<String, Object> values;

  public static FakeConfig create() {
    return create(Collections.emptyMap());
  }

  public static FakeConfig create(Map<String, Object> values) {
    return new FakeConfig(Collections.unmodifiableMap(values));
  }

  public static FakeConfig create(String key, Object value) {
    return create(Maps.newHashMap(key, value));
  }

  private FakeConfig(Map<String, Object> values) {
    this.values = values;
  }

  @Override public void triggerRefresh() {
    // do nothing
  }

  @Override public boolean getBoolean(String key) {
    Object value = values.get(key);
    return value != null && (boolean) value;
  }

  @Override public String getString(String key) {
    Object value = values.get(key);
    if (value != null) {
      return (String) value;
    }

    return "";
  }

  @Override public long getLong(String key) {
    Object value = values.get(key);
    if (value != null) {
      return (long) value;
    }

    return 0;
  }
}
