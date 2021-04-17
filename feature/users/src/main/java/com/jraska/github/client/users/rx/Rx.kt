package com.jraska.github.client.users.rx

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable

fun <T> Observable<T>.toLiveData(): LiveData<T> {
  return toFlowable(BackpressureStrategy.MISSING).toLiveData()
}
