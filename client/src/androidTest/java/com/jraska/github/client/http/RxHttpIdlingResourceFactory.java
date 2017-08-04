package com.jraska.github.client.http;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * We have to use ths instead of Jake's https://github.com/JakeWharton/okhttp-idling-resource,
 * because of thread hopping done by RxJava.
 * <p>
 * There is a time delay before schedulers threads run and before they submit a real request
 * to OkHttp. "com.jakewharton.espresso.OkHttp3IdlingResource" is idle for this
 * period of time, causing tests to fail.
 * We need to make the idling resource not idle earlier, which means onSubscribe,
 * so we need to decorate those RxJava instances.
 */
public final class RxHttpIdlingResourceFactory extends CallAdapter.Factory {
  private final CallAdapter.Factory decoratedFactory;
  private final CountingIdlingResource countingIdlingResource;

  public static RxHttpIdlingResourceFactory create() {
    return new RxHttpIdlingResourceFactory(RxJava2CallAdapterFactory.create());
  }

  private RxHttpIdlingResourceFactory(CallAdapter.Factory decoratedFactory) {
    this.decoratedFactory = decoratedFactory;
    countingIdlingResource = new CountingIdlingResource("rxNetwork");
  }

  public IdlingResource idlingResource() {
    return countingIdlingResource;
  }

  @SuppressWarnings("NullableProblems")
  @Nullable @Override
  public CallAdapter<?, ?> get(Type type, Annotation[] annotations, Retrofit retrofit) {
    CallAdapter<?, ?> callAdapter = decoratedFactory.get(type, annotations, retrofit);

    if (callAdapter == null) {
      return null;
    }

    return new IdlingResourceAdapter(callAdapter, countingIdlingResource);
  }

  static class IdlingResourceAdapter implements CallAdapter<Object, Object> {
    private final CallAdapter decoratedAdapter;
    private final CountingIdlingResource resource;

    IdlingResourceAdapter(CallAdapter<?, ?> decoratedAdapter,
                          CountingIdlingResource resource) {
      this.decoratedAdapter = decoratedAdapter;
      this.resource = resource;
    }

    @Override public Type responseType() {
      return decoratedAdapter.responseType();
    }

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override public Object adapt(Call<Object> call) {
      Object adapted = decoratedAdapter.adapt(call);

      if (adapted instanceof Single) {
        Single single = (Single) adapted;

        return single.doOnSubscribe(disposable -> resource.increment())
          .doAfterTerminate(resource::decrement);
      }

      if (adapted instanceof Observable) {
        Observable observable = (Observable) adapted;

        return observable.doOnSubscribe(disposable -> resource.increment())
          .doAfterTerminate(resource::decrement);
      }

      if (adapted instanceof Completable) {
        Completable completable = (Completable) adapted;

        return completable.doOnSubscribe(disposable -> resource.increment())
          .doAfterTerminate(resource::decrement);
      }

      if (adapted instanceof Flowable) {
        Flowable flowable = (Flowable) adapted;

        return flowable.doOnSubscribe(disposable -> resource.increment())
          .doAfterTerminate(resource::decrement);
      }

      if (adapted instanceof Maybe) {
        Maybe maybe = (Maybe) adapted;

        return maybe.doOnSubscribe(disposable -> resource.increment())
          .doAfterTerminate(resource::decrement);
      }

      throw new UnsupportedOperationException("Sorry, I can't deal with: " + adapted);
    }
  }
}
