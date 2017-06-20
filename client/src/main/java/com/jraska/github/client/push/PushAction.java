package com.jraska.github.client.push;

import com.jraska.github.client.common.Preconditions;

public final class PushAction {
  static final String KEY_ACTION = "action";

  private static final String DEFAULT_ACTION = "message_without_data";
  public static final PushAction DEFAULT = create(DEFAULT_ACTION);

  public final String name;

  private PushAction(String name) {
    this.name = name;
  }

  public static PushAction create(String name) {
    Preconditions.argNotNull(name);
    return new PushAction(name);
  }
}
