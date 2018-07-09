package com.jraska.github.client

import android.arch.lifecycle.*

class TestLiveDateObserver<T> : Observer<T> {
  private val fakeOwner = FakeOwner()
  private val values = ArrayList<T?>()
  private val delegate: Observer<T>

  @Suppress("UNCHECKED_CAST")
  internal constructor() : this(EMPTY_OBSERVER as Observer<T>)

  internal constructor(actual: Observer<T>) {
    this.delegate = actual
  }

  override fun onChanged(t: T?) {
    delegate.onChanged(t)
    values.add(t)
  }

  fun value(): T {
    return values.last()!!
  }

  fun lifecycleOwner(): LifecycleOwner {
    return fakeOwner
  }

  fun dispose() {
    markState(Lifecycle.State.DESTROYED)
    values.clear()
  }

  fun markState(state: Lifecycle.State): TestLiveDateObserver<T> {
    fakeOwner.fakeRegistry.markState(state)
    return this
  }

  companion object {
    private val EMPTY_OBSERVER: Observer<Any> = Observer {}

    fun <T> test(liveData: LiveData<T>): TestLiveDateObserver<T> {
      val observer = create<T>()
      liveData.observe(observer.lifecycleOwner(), observer)
      observer.markState(Lifecycle.State.STARTED)
      return observer
    }

    fun <T> test(liveData: LiveData<T>, delegate: Observer<T>): TestLiveDateObserver<T> {
      val observer = create<T>(delegate)
      liveData.observe(observer.lifecycleOwner(), observer)
      observer.markState(Lifecycle.State.STARTED)
      return observer
    }

    fun <T> create(): TestLiveDateObserver<T> {
      return TestLiveDateObserver()
    }

    fun <T> create(delegate: Observer<T>): TestLiveDateObserver<T> {
      return TestLiveDateObserver(delegate)
    }
  }
}

internal class FakeOwner : LifecycleOwner {
  internal val fakeRegistry = LifecycleRegistry(this)

  override fun getLifecycle(): Lifecycle {
    return fakeRegistry
  }
}

fun <T> LiveData<T>.test(): TestLiveDateObserver<T> {
  return TestLiveDateObserver.test(this)
}

fun <T> LiveData<T>.test(delegate: Observer<T>): TestLiveDateObserver<T> {
  return TestLiveDateObserver.test(this, delegate)
}
