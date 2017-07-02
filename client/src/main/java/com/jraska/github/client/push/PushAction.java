package com.jraska.github.client.push;

import com.jraska.github.client.common.Preconditions;

import java.util.Collections;
import java.util.Map;

public final class PushAction {
  static final String KEY_ACTION = "action";

  private static final String DEFAULT_ACTION = "message_without_data";
  public static final PushAction DEFAULT = create(DEFAULT_ACTION);

  public final String name;
  public final Map<String, String> parameters;

  private PushAction(String name, Map<String, String> parameters) {
    this.name = name;
    this.parameters = parameters;
  }

  public static PushAction create(String name) {
    return create(name, Collections.emptyMap());
  }

  public static PushAction create(String name, Map<String, String> properties) {
    Preconditions.argNotNull(name);
    return new PushAction(name, Collections.unmodifiableMap(properties));
  }
}
