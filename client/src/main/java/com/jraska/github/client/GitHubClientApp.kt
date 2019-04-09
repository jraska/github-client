package com.jraska.github.client

import android.app.Application
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.perf.metrics.AddTrace
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jraska.github.client.common.AppBuildConfig
import com.jraska.github.client.http.DaggerHttpComponent
import com.jraska.github.client.http.HttpComponent
import com.jraska.github.client.http.HttpDependenciesModule
import com.jraska.github.client.push.PushHandler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File

open class GitHubClientApp : Application(), HasViewModelFactory {

  private val appComponent: AppComponent by lazy { componentBuilder().build() }

  override fun factory(): ViewModelProvider.Factory {
    return appComponent.viewModelFactory()
  }

  fun pushHandler(): PushHandler {
    return appComponent.pushHandler()
  }

  @AddTrace(name = "App.onCreate")
  override fun onCreate() {
    super.onCreate()

    initRxAndroidMainThread()

    initFresco()
    initThreeTen()

    appComponent.onAppCreateActions().get().forEach {
      it.onCreate(this)
    }
  }

  private fun initRxAndroidMainThread() {
    RxAndroidPlugins.setInitMainThreadSchedulerHandler {
      AndroidSchedulers.from(Looper.getMainLooper(), true)
    }
  }

  private fun initFresco() {
    Fresco.initialize(this)
  }

  private fun initThreeTen() {
    AndroidThreeTen.init(this)
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
    val dependenciesModule = HttpDependenciesModule(
      AppBuildConfig(BuildConfig.DEBUG), File(cacheDir, "network"))

    return DaggerHttpComponent.builder()
      .httpDependenciesModule(dependenciesModule)
      .build()
  }
}
