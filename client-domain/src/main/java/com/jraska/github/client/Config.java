package com.jraska.github.client;

public interface Config {
  void triggerRefresh();

  boolean getBoolean(String key);
}
