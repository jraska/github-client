package com.jraska.github.client

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.common.AppBuildConfig
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.time.DateTimeProvider
import com.jraska.github.client.time.RealDateTimeProvider
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoSet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class AppModule(private val app: GitHubClientApp) {

  @Provides internal fun provideContext(): Context {
    return app
  }

  @Provides
  @Reusable internal fun provideConfig(): AppBuildConfig {
    return AppBuildConfig(BuildConfig.DEBUG)
  }

  @Provides
  @PerApp internal fun topActivityProvider(): TopActivityProvider {
    return TopActivityProvider()
  }

  @Provides
  @IntoSet
  fun topActivityOnCreate(setup: TopActivityProvider.OnCreateSetup): OnAppCreate {
    return setup
  }

  @Provides
  @PerApp internal fun provideLayoutInflater(context: Context): LayoutInflater {
    return LayoutInflater.from(context)
  }

  @Provides
  internal fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory {
    return factory
  }

  @Provides
  @PerApp internal fun dateTimeProvider(): DateTimeProvider {
    return RealDateTimeProvider()
  }

  @Provides internal fun notificationManager(): NotificationManager {
    return app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  }

  @Provides
  @PerApp
  fun schedulers(): AppSchedulers {
    return AppSchedulers(AndroidSchedulers.mainThread(),
      Schedulers.io(), Schedulers.computation())
  }

  @Provides
  @IntoSet
  internal fun setupLoggingOnCreate(setupLogging: SetupLogging): OnAppCreate {
    return setupLogging
  }

  @Provides
  @IntoSet
  fun reportAppCreateEvent(eventAnalytics: EventAnalytics): OnAppCreate {
    return object: OnAppCreate {
      override fun onCreate(app: Application) {
        val createEvent = AnalyticsEvent.create("app_create")
        eventAnalytics.report(createEvent)
      }
    }
  }

}
