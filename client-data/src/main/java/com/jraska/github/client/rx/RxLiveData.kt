package com.jraska.github.client.rx

import androidx.lifecycle.LiveData
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

class RxLiveData<T> internal constructor(private val subscriberAdapter: SubscriberAdapter<T>) : LiveData<T>() {

  private var subscription: Disposable? = null

  override fun onActive() {
    super.onActive()
    if (subscription == null) {
      subscription = subscribe()
    }
  }

  override fun onInactive() {
    dispose()
    super.onInactive()
  }

  fun resubscribe() {
    if (subscription != null) {
      dispose()
      subscription = subscribe()
    }
  }

  private fun dispose() {
    if (subscription != null) {
      subscription!!.dispose()
      subscription = null
    }
  }

  private fun subscribe(): Disposable {
    return subscriberAdapter.subscribe(Consumer { this.value = it })
  }

  interface SubscriberAdapter<T> {
    fun subscribe(onValue: Consumer<T>): Disposable
  }

  companion object {
    fun <T> from(observable: Observable<T>): RxLiveData<T> {
      return create(object : SubscriberAdapter<T> {
        override fun subscribe(onValue: Consumer<T>): Disposable {
          return observable.subscribe(onValue)
        }
      })
    }

    fun <T> from(single: Single<T>): RxLiveData<T> {
      return create(object : SubscriberAdapter<T> {
        override fun subscribe(onValue: Consumer<T>): Disposable {
          return single.subscribe(onValue)
        }
      })
    }

    fun <T> from(maybe: Maybe<T>): RxLiveData<T> {
      return create(object : SubscriberAdapter<T> {
        override fun subscribe(onValue: Consumer<T>): Disposable {
          return maybe.subscribe(onValue)
        }
      })
    }

    private fun <T> create(adapter: SubscriberAdapter<T>): RxLiveData<T> {
      return RxLiveData(adapter)
    }
  }
}
