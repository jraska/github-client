package com.jraska.github.client.rx;

import android.arch.lifecycle.LiveData;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class RxLiveData<T> {
  public static <T> SingleAdapter<T> adapt(Single<T> single) {
    return new SingleAdapter<>(single);
  }

  public static <T> ObservableAdapter<T> adapt(Observable<T> observable) {
    return new ObservableAdapter<>(observable);
  }

  public static final class SingleAdapter<T> extends LiveData<T> {
    private final Single<T> single;
    private Disposable subscription;

    private SingleAdapter(Single<T> single) {
      this.single = single;
    }

    @Override protected void onActive() {
      super.onActive();
      subscribe();
    }

    @Override protected void onInactive() {
      dispose();
      super.onInactive();
    }

    private void subscribe() {
      // No error handling since LiveData do not support that. Error handling needs to be done elsewhere
      subscription = single.subscribe(SingleAdapter.this::setValueInternal);
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

    void setValueInternal(T value) {
      setValue(value);
    }
  }

  public static final class ObservableAdapter<T> extends LiveData<T> {
    private final Observable<T> observable;
    private Disposable subscription;

    private ObservableAdapter(Observable<T> observable) {
      this.observable = observable;
    }

    @Override protected void onActive() {
      super.onActive();

      // No error handling since LiveData do not support that. Error handling needs to be done elsewhere
      subscription = observable.subscribe(ObservableAdapter.this::setValueInternal);
    }

    @Override protected void onInactive() {
      subscription.dispose();
      subscription = null;

      super.onInactive();
    }

    void setValueInternal(T value) {
      setValue(value);
    }
  }
}
