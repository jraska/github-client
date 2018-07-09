package com.jraska.livedata;

import android.arch.lifecycle.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class TestLiveDataObserver<T> implements Observer<T> {
  private static final Observer<Object> EMPTY_OBSERVER = t -> {
  };

  private final FakeOwner fakeOwner = new FakeOwner();
  private final List<T> valuesHistory = new ArrayList<>();
  private final Observer<? super T> delegate;

  private TestLiveDataObserver() {
    this(EMPTY_OBSERVER);
  }

  private TestLiveDataObserver(Observer<? super T> delegate) {
    this.delegate = delegate;
  }

  @Override public void onChanged(@Nullable T value) {
    valuesHistory.add(value);
    delegate.onChanged(value);
  }

  @NonNull public LifecycleOwner lifecycleOwner() {
    return fakeOwner;
  }

  public TestLiveDataObserver<T> markState(Lifecycle.State state) {
    fakeOwner.fakeRegistry.markState(state);
    return this;
  }

  public TestLiveDataObserver<T> dispose() {
    valuesHistory.clear();
    return markState(Lifecycle.State.DESTROYED);
  }

  public T value() {
    assertHasValue();
    return valuesHistory.get(valuesHistory.size() - 1);
  }

  public TestLiveDataObserver<T> assertHasValue() {
    if (valuesHistory.isEmpty()) {
      throw fail("Observer never received any value");
    }

    return this;
  }

  private AssertionError fail(String message) {
    return new AssertionError(message);
  }


  public static <T> TestLiveDataObserver<T> create() {
    return new TestLiveDataObserver<>();
  }

  public static <T> TestLiveDataObserver<T> create(Observer<T> delegate) {
    return new TestLiveDataObserver<>(delegate);
  }

  public static <T> TestLiveDataObserver<T> test(LiveData<T> liveData) {
    TestLiveDataObserver<T> observer = create();
    liveData.observe(observer.lifecycleOwner(), observer);
    return observer.markState(Lifecycle.State.STARTED);
  }

  public static <T> TestLiveDataObserver<T> test(LiveData<T> liveData, Observer<T> delegate) {
    TestLiveDataObserver<T> observer = create(delegate);
    liveData.observe(observer.lifecycleOwner(), observer);
    return observer.markState(Lifecycle.State.STARTED);
  }

  static final class FakeOwner implements LifecycleOwner {
    private final LifecycleRegistry fakeRegistry = new LifecycleRegistry(this);

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
      return fakeRegistry;
    }
  }
}
