package com.jraska.github.client

import android.app.Application
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.perf.metrics.AddTrace
import com.jraska.github.client.core.android.HasServiceModelFactory
import com.jraska.github.client.core.android.HasViewModelFactory
import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.http.DaggerHttpComponent
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File

open class GitHubClientApp : Application(), HasViewModelFactory, HasServiceModelFactory {

  private val appComponent: AppComponent by lazy { componentBuilder().build() }

  override fun factory(): ViewModelProvider.Factory {
    return appComponent.viewModelFactory()
  }

  override fun serviceModelFactory(): ServiceModel.Factory {
    return appComponent.serviceModelFactory()
  }

  @AddTrace(name = "App.onCreate")
  override fun onCreate() {
    super.onCreate()

    initRxAndroidMainThread() // Must be here, otherwise is fragile due to class loading

    appComponent.onAppCreateActions()
      .sortedByDescending { it.priority() }
      .forEach {
        it.onCreate(this)
      }
  }

  private fun componentBuilder(): AppComponent.Builder {
    return DaggerAppComponent.builder()
      .appContext(this)
      .httpComponent(retrofit())
      .coreComponent(coreComponent())
  }

  protected open fun coreComponent(): CoreComponent {
    return DaggerFirebaseCoreComponent.builder().build()
  }

  protected open fun retrofit(): HasRetrofit {
    return DaggerHttpComponent.builder()
      .cacheDir(File(cacheDir, "network"))
      .logger(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
          Timber.tag("Network").v(message)
        }
      })
      .build()
  }

  private fun initRxAndroidMainThread() {
    RxAndroidPlugins.setInitMainThreadSchedulerHandler {
      AndroidSchedulers.from(Looper.getMainLooper(), true)
    }
  }
}
