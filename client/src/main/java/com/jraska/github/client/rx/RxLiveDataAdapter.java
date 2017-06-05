package com.jraska.github.client.rx;

import android.arch.lifecycle.LiveData;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class RxLiveDataAdapter<T> {
  public static <T> LiveData<T> adapt(Single<T> single) {
    return new SingleAdapter<>(single);
  }

  static class SingleAdapter<T> extends LiveData<T> {
    private final Single<T> single;
    private Disposable subscription;

    private SingleAdapter(Single<T> single) {
      this.single = single;
    }

    @Override protected void onActive() {
      super.onActive();

      // No error handling since LiveData do not support that. Error handling needs to be done elsewhere
      subscription = single.subscribe(SingleAdapter.this::setValueInternal);
    }

    @Override protected void onInactive() {
      subscription.dispose();

      super.onInactive();
    }

    // Need to bridge the method, otherwise we get IllegalAccessError with emthod reference
    void setValueInternal(T value) {
      setValue(value);
    }
  }
}
