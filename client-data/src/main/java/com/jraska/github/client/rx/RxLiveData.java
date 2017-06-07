package com.jraska.github.client.rx;

import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public abstract class RxLiveData<T> extends LiveData<T> {
  public static <T> RxLiveData<T> from(Single<T> single) {
    return new SingleAdapter<>(single);
  }

  public static <T> RxLiveData<T> from(Observable<T> observable) {
    return new ObservableAdapter<>(observable);
  }

  @Nullable private Disposable subscription;

  RxLiveData() {
  }

  @Override protected void onActive() {
    super.onActive();
    if (subscription == null) {
      subscription = subscribe();
    }
  }

  @Override protected void onInactive() {
    super.onInactive();
    dispose();
  }

  public RxLiveData<T> resubscribe() {
    if (subscription != null) {
      dispose();
      subscription = subscribe();
    }

    return this;
  }

  private void dispose() {
    if (subscription != null) {
      subscription.dispose();
      subscription = null;
    }
  }

  abstract Disposable subscribe();

  void setValueInternal(T value) {
    setValue(value);
  }

  static final class SingleAdapter<T> extends RxLiveData<T> {
    private final Single<T> single;

    private SingleAdapter(Single<T> single) {
      this.single = single;
    }

    @Override
    Disposable subscribe() {
      return single.subscribe(this::setValueInternal);
    }
  }

  static final class ObservableAdapter<T> extends RxLiveData<T> {
    private final Observable<T> observable;

    private ObservableAdapter(Observable<T> observable) {
      this.observable = observable;
    }

    @Override Disposable subscribe() {
      return observable.subscribe(this::setValueInternal);
    }
  }
}
