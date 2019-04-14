package com.jraska.github.client

import android.app.Application
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.perf.metrics.AddTrace
import com.jraska.github.client.core.android.HasViewModelFactory
import com.jraska.github.client.http.DaggerHttpComponent
import com.jraska.github.client.http.HttpComponent
import com.jraska.github.client.push.HasPushHandler
import com.jraska.github.client.push.PushHandler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File

open class GitHubClientApp : Application(), HasViewModelFactory, HasPushHandler {

  private val appComponent: AppComponent by lazy { componentBuilder().build() }

  override fun factory(): ViewModelProvider.Factory {
    return appComponent.viewModelFactory()
  }

  override fun pushHandler(): PushHandler {
    return appComponent.pushHandler()
  }

  @AddTrace(name = "App.onCreate")
  override fun onCreate() {
    super.onCreate()

    initRxAndroidMainThread()
    appComponent.onAppCreateActions().forEach {
      it.onCreate(this)
    }
  }

  private fun initRxAndroidMainThread() {
    RxAndroidPlugins.setInitMainThreadSchedulerHandler {
      AndroidSchedulers.from(Looper.getMainLooper(), true)
    }
  }

  private fun componentBuilder(): DaggerAppComponent.Builder {
    return DaggerAppComponent.builder()
      .appModule(AppModule(this))
      .httpComponentModule(HttpComponentModule(httpComponent()))
      .coreComponentModule(CoreComponentModule(coreComponent()))
  }

  protected open fun coreComponent(): CoreComponent {
    return DaggerCoreComponent.builder().build()
  }

  protected open fun httpComponent(): HttpComponent {
    return DaggerHttpComponent.builder()
      .cacheDir(File(cacheDir, "network"))
      .logger(HttpLoggingInterceptor.Logger { Timber.tag("Network").v(it) })
      .build()
  }
}
