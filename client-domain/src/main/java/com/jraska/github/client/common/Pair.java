package com.jraska.github.client.common;

public final class Pair<F, S> {
  public final F first;
  public final S second;

  public Pair(F first, S second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pair)) {
      return false;
    }
    Pair<?, ?> p = (Pair<?, ?>) o;
    return objectsEqual(p.first, first) && objectsEqual(p.second, second);
  }

  private static boolean objectsEqual(Object a, Object b) {
    return a == b || (a != null && a.equals(b));
  }

  @Override
  public int hashCode() {
    return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
  }
}
