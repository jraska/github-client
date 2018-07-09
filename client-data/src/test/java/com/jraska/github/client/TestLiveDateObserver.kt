package com.jraska.github.client

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.jraska.livedata.TestLiveDataObserver

fun <T> LiveData<T>.test(): TestLiveDataObserver<T> {
  return TestLiveDataObserver.test(this)
}
