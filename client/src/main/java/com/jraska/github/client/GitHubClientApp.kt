package com.jraska.github.client

import android.app.Application
import android.content.Context
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.firebase.perf.metrics.AddTrace
import com.jraska.github.client.core.android.HasServiceModelFactory
import com.jraska.github.client.core.android.HasViewModelFactory
import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.http.DaggerHttpComponent
import com.jraska.github.client.http.HttpComponent
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File

open class GitHubClientApp : Application(), HasViewModelFactory, HasServiceModelFactory, HasDynamicFeaturesComponent {

  private val appComponent: AppComponent by lazy { componentBuilder().build() }

  override fun factory(): ViewModelProvider.Factory {
    return appComponent.viewModelFactory()
  }

  override fun serviceModelFactory(): ServiceModel.Factory {
    return appComponent.serviceModelFactory()
  }

  override fun dynamicFeaturesComponent(): DynamicFeaturesComponent {
    return appComponent.dynamicFeaturesComponent()
  }

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
    // Emulates installation of future on demand modules using SplitCompat.
    SplitCompat.install(this)
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

  private fun componentBuilder(): AppComponent.Builder {
    return DaggerAppComponent.builder()
      .appContext(this)
      .httpComponent(HttpComponentDelegate(httpComponent()))
      .coreComponent(CoreComponentDelegate(coreComponent()))
  }

  protected open fun coreComponent(): CoreComponent {
    return DaggerCoreComponent.builder().build()
  }

  protected open fun httpComponent(): HttpComponent {
    return DaggerHttpComponent.builder()
      .cacheDir(File(cacheDir, "network"))
      .logger(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
          Timber.tag("Network").v(message)
        }
      })
      .build()
  }
}
