package com.jraska.github.client.rx;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public abstract class RxLiveData<T> extends LiveData<T> {
  public static <T> RxLiveData<T> from(Single<T> single) {
    return new SingleAdapter<>(single);
  }

  public static <T> RxLiveData<T> from(Observable<T> observable) {
    return new ObservableAdapter<>(observable);
  }

  protected Consumer<? super Throwable> onError;
  @Nullable private Disposable subscription;

  RxLiveData() {
  }

  @Override protected void onActive() {
    super.onActive();
    subscription = subscribe();
  }

  @Override protected void onInactive() {
    dispose();
    super.onInactive();
  }

  public RxLiveData<T> resubscribe() {
    if (subscription != null) {
      dispose();
      subscribe();
    }

    return this;
  }

  // TODO: 07/06/17 Error handling exposes RxLiveData everywhere. Too invasive
  // Solution is to make LiveData never fail -> LiveData<ViewState>
  public RxLiveData<T> observe(LifecycleOwner owner, Observer<T> observer,
                               Consumer<? super Throwable> onError) {
    this.onError = onError;
    super.observe(owner, observer);
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
      if (onError == null) {
        return single.subscribe(this::setValueInternal);
      } else {
        return single.subscribe(this::setValueInternal, onError);
      }
    }
  }

  static final class ObservableAdapter<T> extends RxLiveData<T> {
    private final Observable<T> observable;

    private ObservableAdapter(Observable<T> observable) {
      this.observable = observable;
    }

    @Override Disposable subscribe() {
      if (onError == null) {
        return observable.subscribe(this::setValueInternal);
      } else {
        return observable.subscribe(this::setValueInternal, onError);
      }
    }
  }
}
