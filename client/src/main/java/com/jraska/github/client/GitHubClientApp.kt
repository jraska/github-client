package com.jraska.github.client

import android.app.Application
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.perf.metrics.AddTrace
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.common.AppBuildConfig
import com.jraska.github.client.http.DaggerHttpComponent
import com.jraska.github.client.http.HttpComponent
import com.jraska.github.client.http.HttpDependenciesModule
import com.jraska.github.client.push.PushCallbacks
import com.jraska.github.client.push.PushHandler
import com.jraska.github.client.push.PushIntentObserver
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import javax.inject.Inject

open class GitHubClientApp : Application(), HasViewModelFactory {

  @Inject internal lateinit var pushHandler: PushHandler
  @Inject internal lateinit var notificationSetup: NotificationSetup

  private val appComponent: AppComponent by lazy { componentBuilder().build() }

  override fun factory(): ViewModelProvider.Factory {
    return appComponent.viewModelFactory()
  }

  fun pushHandler(): PushHandler {
    return pushHandler
  }

  @AddTrace(name = "App.onCreate")
  override fun onCreate() {
    super.onCreate()

    initRxAndroidMainThread()

    appComponent.inject(this)
    appComponent.onAppCreateActions().get().forEach {
      it.onCreate(this)
    }

    initFresco()
    initThreeTen()

    notificationSetup.setupChannels()

    registerActivityLifecycleCallbacks(PushCallbacks(PushIntentObserver(pushHandler())))
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
