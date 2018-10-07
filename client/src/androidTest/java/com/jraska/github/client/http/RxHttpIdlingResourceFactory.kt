package com.jraska.github.client.http

import android.support.test.espresso.IdlingResource
import android.support.test.espresso.idling.CountingIdlingResource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

/**
 * We have to use ths instead of Jake's https://github.com/JakeWharton/okhttp-idling-resource,
 * because of thread hopping done by RxJava.
 *
 *
 * There is a time delay before schedulers threads run and before they submit a real request
 * to OkHttp. "com.jakewharton.espresso.OkHttp3IdlingResource" is idle for this
 * period of time, causing tests to fail.
 * We need to make the idling resource not idle earlier, which means onSubscribe,
 * so we need to decorate those RxJava instances.
 */
class RxHttpIdlingResourceFactory private constructor(private val decoratedFactory: CallAdapter.Factory) : CallAdapter.Factory() {
  private val countingIdlingResource: CountingIdlingResource = CountingIdlingResource("rxNetwork")

  fun idlingResource(): IdlingResource {
    return countingIdlingResource
  }

  @Suppress("UNCHECKED_CAST")
  override fun get(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
    val callAdapter = decoratedFactory.get(type, annotations, retrofit) ?: return null

    return IdlingResourceAdapter(callAdapter as CallAdapter<Any, Any>, countingIdlingResource)
  }

  internal class IdlingResourceAdapter(
    private val decoratedAdapter: CallAdapter<Any, Any>,
    private val resource: CountingIdlingResource
  ) : CallAdapter<Any, Any> {

    override fun responseType(): Type {
      return decoratedAdapter.responseType()
    }

    override fun adapt(call: Call<Any>): Any {
      val adapted = decoratedAdapter.adapt(call)

      if (adapted is Single<*>) return adapted.doOnSubscribe { _ -> resource.increment() }
        .doAfterTerminate { resource.decrement() }

      if (adapted is Observable<*>) {

        return adapted.doOnSubscribe { _ -> resource.increment() }
          .doAfterTerminate { resource.decrement() }
      }

      if (adapted is Completable) {

        return adapted.doOnSubscribe { _ -> resource.increment() }
          .doAfterTerminate { resource.decrement() }
      }

      if (adapted is Flowable<*>) {

        return adapted.doOnSubscribe { _ -> resource.increment() }
          .doAfterTerminate { resource.decrement() }
      }

      if (adapted is Maybe<*>) {

        return adapted.doOnSubscribe { _ -> resource.increment() }
          .doAfterTerminate { resource.decrement() }
      }

      throw UnsupportedOperationException("Sorry, I can't deal with: $adapted")
    }
  }

  companion object {

    fun create(): RxHttpIdlingResourceFactory {
      return RxHttpIdlingResourceFactory(RxJava2CallAdapterFactory.create())
    }
  }
}
