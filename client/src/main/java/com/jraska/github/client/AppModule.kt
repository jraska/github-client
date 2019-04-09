package com.jraska.github.client

import android.app.NotificationManager
import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.jraska.console.timber.ConsoleTree
import com.jraska.github.client.common.AppBuildConfig
import com.jraska.github.client.logging.ErrorReportTree
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.time.DateTimeProvider
import com.jraska.github.client.time.RealDateTimeProvider
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoSet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Provider

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
  @IntoSet
  internal fun setupLoggingOnCreate(setupLogging: SetupLogging): OnAppCreate {
    return setupLogging
  }

  @Provides
  @PerApp
  fun schedulers(): AppSchedulers {
    return AppSchedulers(AndroidSchedulers.mainThread(),
      Schedulers.io(), Schedulers.computation())
  }
}
