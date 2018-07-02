package com.jraska.github.client

import android.arch.lifecycle.*

class TestLiveDateObserver<T> : Observer<T>, LifecycleOwner {
  private val fakeRegistry = LifecycleRegistry(this)
  private val values = ArrayList<T?>()

  override fun getLifecycle(): Lifecycle {
    return fakeRegistry
  }

  override fun onChanged(t: T?) {
    values.add(t)
  }

  fun value(): T {
    return values.last()!!
  }

  fun dispose() {
    markState(Lifecycle.State.DESTROYED)
  }

  fun markState(state: Lifecycle.State): TestLiveDateObserver<T> {
    fakeRegistry.markState(state)
    return this
  }
}

fun <T> LiveData<T>.test(): TestLiveDateObserver<T> {
  val observer = TestLiveDateObserver<T>()
  observe(observer, observer)
  observer.markState(Lifecycle.State.STARTED)
  return observer
}
