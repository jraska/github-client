package com.jraska.github.client.users.rx

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable

fun <T> Observable<T>.toLiveData(): LiveData<T> {
  return toFlowable(BackpressureStrategy.MISSING).toLiveData()
}
