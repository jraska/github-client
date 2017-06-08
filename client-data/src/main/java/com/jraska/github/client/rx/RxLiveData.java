package com.jraska.github.client.rx;

import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public final class RxLiveData<T> extends LiveData<T> {
  public static <T> RxLiveData<T> from(Observable<T> observable) {
    return create(new ObservableAdapter<>(observable));
  }

  public static <T> RxLiveData<T> from(Single<T> single) {
    return create(new SingleAdapter<>(single));
  }

  public static <T> RxLiveData<T> from(Maybe<T> maybe) {
    return create(new MaybeAdapter<>(maybe));
  }

  private static <T> RxLiveData<T> create(SubscriberAdapter<T> adapter) {
    return new RxLiveData<>(adapter);
  }

  private final SubscriberAdapter<T> subscriberAdapter;

  @Nullable private Disposable subscription;

  RxLiveData(SubscriberAdapter<T> subscriberAdapter) {
    this.subscriberAdapter = subscriberAdapter;
  }

  @Override protected void onActive() {
    super.onActive();
    if (subscription == null) {
      subscription = subscribe();
    }
  }

  @Override protected void onInactive() {
    dispose();
    super.onInactive();
  }

  public void resubscribe() {
    if (subscription != null) {
      dispose();
      subscription = subscribe();
    }
  }

  private void dispose() {
    if (subscription != null) {
      subscription.dispose();
      subscription = null;
    }
  }

  private Disposable subscribe() {
    return subscriberAdapter.subscribe(this::setValueInternal);
  }

  void setValueInternal(T value) {
    setValue(value);
  }

  interface SubscriberAdapter<T> {
    Disposable subscribe(Consumer<T> onValue);
  }

  static final class ObservableAdapter<T> implements SubscriberAdapter<T> {
    private final Observable<T> observable;

    ObservableAdapter(Observable<T> observable) {
      this.observable = observable;
    }

    @Override public Disposable subscribe(Consumer<T> onNext) {
      return observable.subscribe(onNext);
    }
  }

  static final class SingleAdapter<T> implements SubscriberAdapter<T> {
    private final Single<T> single;

    SingleAdapter(Single<T> single) {
      this.single = single;
    }

    @Override public Disposable subscribe(Consumer<T> onSuccess) {
      return single.subscribe(onSuccess);
    }
  }

  static final class MaybeAdapter<T> implements SubscriberAdapter<T> {
    private final Maybe<T> maybe;

    MaybeAdapter(Maybe<T> maybe) {
      this.maybe = maybe;
    }

    @Override public Disposable subscribe(Consumer<T> onSuccess) {
      return maybe.subscribe(onSuccess);
    }
  }
}
