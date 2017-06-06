package com.jraska.github.client.rx;

import android.arch.lifecycle.LiveData;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public abstract class RxLiveData<T> extends LiveData<T> {
  public static <T> RxLiveData<T> adapt(Single<T> single) {
    return new SingleAdapter<>(single);
  }

  public static <T> RxLiveData<T> adapt(Observable<T> observable) {
    return new ObservableAdapter<>(observable);
  }

  private Disposable subscription;

  @Override protected void onActive() {
    super.onActive();
    subscription = subscribe();
  }

  @Override protected void onInactive() {
    dispose();
    super.onInactive();
  }

  private void dispose() {
    subscription.dispose();
    subscription = null;
  }

  public void resubscribe() {
    if (subscription != null) {
      dispose();
      subscribe();
    }
  }

  protected abstract Disposable subscribe();

  void setValueInternal(T value) {
    setValue(value);
  }

  static final class SingleAdapter<T> extends RxLiveData<T> {
    private final Single<T> single;

    private SingleAdapter(Single<T> single) {
      this.single = single;
    }

    @Override
    public Disposable subscribe() {
      // No error handling since LiveData do not support that. Error handling needs to be done elsewhere
      return single.subscribe(SingleAdapter.this::setValueInternal);
    }
  }

  static final class ObservableAdapter<T> extends RxLiveData<T> {
    private final Observable<T> observable;

    private ObservableAdapter(Observable<T> observable) {
      this.observable = observable;
    }

    @Override protected Disposable subscribe() {
      return observable.subscribe(ObservableAdapter.this::setValueInternal);
    }
  }
}
