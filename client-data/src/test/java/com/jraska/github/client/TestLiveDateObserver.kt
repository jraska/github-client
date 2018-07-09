package com.jraska.github.client

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.jraska.livedata.TestLiveDataObserver

fun <T> LiveData<T>.test(): TestLiveDataObserver<T> {
  return TestLiveDataObserver.test(this)
}

fun <T> LiveData<T>.test(delegate: Observer<T>): TestLiveDataObserver<T> {
  return TestLiveDataObserver.test(this, delegate)
}
