package com.jraska.github.client.core.android

import android.app.Application
import com.jraska.github.client.coroutines.AppDispatchers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class OnAppCreateAsyncExecutor @Inject constructor(
  private val asyncActions: @JvmSuppressWildcards Set<OnAppCreateAsync>,
  private val appDispatchers: AppDispatchers
) : OnAppCreate {

  @DelicateCoroutinesApi // this is app init
  override fun onCreate(app: Application) {
    GlobalScope.launch(appDispatchers.io) {
      // FIXME: 18/9/21 run in parallel
      asyncActions.forEach { appCreateAsync ->
        try {
          appCreateAsync.onCreateAsync(app)
        } catch (ex: Exception) {
          crashTheApp(ex)
        }
      }
    }
  }

  // From https://github.com/ReactiveX/RxJava/blob/0df952e007814da9f2d4566097676590b977c708/src/main/java/io/reactivex/rxjava3/plugins/RxJavaPlugins.java#L431
  private fun crashTheApp(error: Throwable) {
    val currentThread = Thread.currentThread()
    val handler = currentThread.uncaughtExceptionHandler!!
    handler.uncaughtException(currentThread, error)
  }
}
