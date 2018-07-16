package com.jraska.github.client

import android.arch.lifecycle.LiveData
import com.jraska.livedata.TestObserver

fun <T> LiveData<T>.test(): TestObserver<T> {
  return TestObserver.test(this)
}
