package com.jraska.github.client.common;

import java.util.HashMap;
import java.util.Map;

public final class Maps {
  private Maps() {
    Preconditions.throwNoInstances();
  }

  public static <K, V> Map<K, V> newHashMap(K key, V value) {
    HashMap<K, V> map = new HashMap<>();
    map.put(key, value);
    return map;
  }
}
