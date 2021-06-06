package com.jraska.github.client.core.android

import android.app.Application
import com.jraska.github.client.rx.AppSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.internal.functions.Functions
import javax.inject.Inject

class OnAppCreateAsyncExecutor @Inject constructor(
  private val asyncActions: @JvmSuppressWildcards Set<OnAppCreateAsync>,
  private val appSchedulers: AppSchedulers
) : OnAppCreate {
  override fun onCreate(app: Application) {
    asyncActions.forEach { appCreateAsync ->
      Completable.fromAction { appCreateAsync.onCreateAsync(app) }
        .subscribeOn(appSchedulers.computation)
        .subscribe(Functions.EMPTY_ACTION, { crashTheApp(it) })
    }
  }

  // From https://github.com/ReactiveX/RxJava/blob/0df952e007814da9f2d4566097676590b977c708/src/main/java/io/reactivex/rxjava3/plugins/RxJavaPlugins.java#L431
  private fun crashTheApp(error: Throwable) {
    val currentThread = Thread.currentThread()
    val handler = currentThread.uncaughtExceptionHandler!!
    handler.uncaughtException(currentThread, error)
  }
}
