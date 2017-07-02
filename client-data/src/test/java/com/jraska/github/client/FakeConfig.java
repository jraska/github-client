package com.jraska.github.client;

import java.util.Collections;
import java.util.HashMap;
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
    Map<String, Object> map = new HashMap<>();
    map.put(key, value);

    return create(map);
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

  @Override public long getLong(String key) {
    Object value = values.get(key);
    if (value != null) {
      return (long) value;
    }

    return 0;
  }
}
