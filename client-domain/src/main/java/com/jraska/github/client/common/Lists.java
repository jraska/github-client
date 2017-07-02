package com.jraska.github.client.common;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

public final class Lists {
  private Lists() {
    Preconditions.throwNoInstances();
  }

  public static <R, T> List<R> transform(List<T> list, Function<T, R> transform) {
    List<R> newList = new ArrayList<>();
    for (T item : list) {
      try {
        newList.add(transform.apply(item));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    return newList;
  }
}
