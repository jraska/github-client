package com.jraska.livedata;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TestObserver<T> implements Observer<T> {
  private final List<T> valuesHistory = new ArrayList<>();
  private final LiveData<T> observedLiveData;

  private TestObserver(LiveData<T> observedLiveData) {
    this.observedLiveData = observedLiveData;
  }

  @Override public void onChanged(@Nullable T value) {
    valuesHistory.add(value);
  }

  public T value() {
    assertHasValue();
    return valuesHistory.get(valuesHistory.size() - 1);
  }

  public List<T> valuesHistory() {
    return Collections.unmodifiableList(new ArrayList<>(valuesHistory));
  }

  public TestObserver<T> dispose() {
    valuesHistory.clear();
    observedLiveData.removeObserver(this);
    return this;
  }

  public TestObserver<T> assertHasValue() {
    if (valuesHistory.isEmpty()) {
      throw fail("Observer never received any value");
    }

    return this;
  }

  public TestObserver<T> assertNoValues() {
    return assertValueCount(0);
  }

  public TestObserver<T> assertValueCount(int count) {
    int size = valuesHistory.size();
    if (size != count) {
      throw fail("Value counts differ; Expected: " + count + ", Actual: " + size);
    }
    return this;
  }

  public TestObserver<T> assertValue(T expected) {
    T value = value();

    if (expected == null && value == null) {
      return this;
    }

    if (!value.equals(expected)) {
      throw fail("Expected: " + valueAndClass(expected) + ", Actual: " + valueAndClass(value));
    }

    return this;
  }

  public TestObserver<T> assertValue(Function<T, Boolean> valuePredicate) {
    T value = value();

    if (!valuePredicate.apply(value)) {
      throw fail("Value not present");
    }

    return this;
  }

  public TestObserver<T> assertNever(Function<T, Boolean> valuePredicate) {
    int size = valuesHistory.size();
    for (int valueIndex = 0; valueIndex < size; valueIndex++) {
      T value = this.valuesHistory.get(valueIndex);
      if (valuePredicate.apply(value)) {
        throw fail("Value at position " + valueIndex + " matches predicate "
          + valuePredicate.toString() + ", which was not expected.");
      }
    }

    return this;
  }

  private AssertionError fail(String message) {
    return new AssertionError(message);
  }

  private static String valueAndClass(Object value) {
    if (value != null) {
      return value + " (class: " + value.getClass().getSimpleName() + ")";
    }
    return "null";
  }

  public static <T> TestObserver<T> create() {
    return new TestObserver<>(new MutableLiveData<>());
  }

  public static <T> TestObserver<T> test(LiveData<T> liveData) {
    TestObserver<T> observer = new TestObserver<>(liveData);
    liveData.observeForever(observer);
    return observer;
  }
}
