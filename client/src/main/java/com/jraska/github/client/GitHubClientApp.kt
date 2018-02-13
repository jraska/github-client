package com.jraska.github.client

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.perf.metrics.AddTrace
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.common.AppBuildConfig
import com.jraska.github.client.http.DaggerHttpComponent
import com.jraska.github.client.http.HttpComponent
import com.jraska.github.client.http.HttpDependenciesModule
import com.jraska.github.client.logging.ErrorReportTree
import com.jraska.github.client.push.PushCallbacks
import com.jraska.github.client.push.PushHandler
import com.jraska.github.client.push.PushIntentObserver
import timber.log.Timber
import java.io.File
import javax.inject.Inject

open class GitHubClientApp : Application() {

  @Inject internal lateinit var eventAnalytics: EventAnalytics
  @Inject internal lateinit var errorReportTree: ErrorReportTree
  @Inject internal lateinit var topActivityProvider: TopActivityProvider
  @Inject internal lateinit var viewModelFactory: ViewModelProvider.Factory
  @Inject internal lateinit var pushHandler: PushHandler
  @Inject internal lateinit var notificationSetup: NotificationSetup

  fun viewModelFactory(): ViewModelProvider.Factory {
    return viewModelFactory
  }

  fun pushHandler(): PushHandler {
    return pushHandler
  }

  @AddTrace(name = "App.onCreate")
  override fun onCreate() {
    super.onCreate()

    val appComponent = componentBuilder().build()
    appComponent.inject(this)

    initFresco()
    initThreeTen()

    Timber.plant(errorReportTree)
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    notificationSetup.setupChannels()

    registerActivityLifecycleCallbacks(topActivityProvider.callbacks)

    registerActivityLifecycleCallbacks(PushCallbacks(PushIntentObserver(pushHandler())))

    logAppCreateEvent()
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

  private fun logAppCreateEvent() {
    val createEvent = AnalyticsEvent.create("app_create")
    eventAnalytics.report(createEvent)
  }
}
