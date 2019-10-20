package com.jraska.github.client.core.android

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jraska.github.client.DeepLinkLauncher
import com.jraska.github.client.PerApp
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.core.android.logging.SetupLogging
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.time.DateTimeProvider
import com.jraska.github.client.time.RealDateTimeProvider
import com.jraska.github.client.time.RealTimeProvider
import com.jraska.github.client.time.TimeProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
object CoreAndroidModule {
  @Provides
  @PerApp
  @JvmStatic
  internal fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory {
    return factory
  }

  @Provides
  @PerApp
  @JvmStatic
  internal fun provideServiceModelFactory(factory: ServiceModelFactory): ServiceModel.Factory {
    return factory
  }

  @JvmStatic
  @Provides
  fun bindDeepLinkLauncher(
    topActivityProvider: TopActivityProvider,
    launchers: @JvmSuppressWildcards Set<LinkLauncher>
  ): DeepLinkLauncher {
    return RealDeepLinkLauncher.create(topActivityProvider, launchers)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @ClassKey(UriHandlerViewModel::class)
  fun uriHandlerViewModel(viewModel: UriHandlerViewModel): ViewModel {
    return viewModel
  }

  @Provides
  @JvmStatic
  @PerApp
  internal fun topActivityProvider(): TopActivityProvider {
    return TopActivityProvider()
  }

  @Provides
  @IntoSet
  @JvmStatic
  fun topActivityOnCreate(setup: TopActivityProvider.RegisterCallbacks): OnAppCreate {
    return setup
  }

  @JvmStatic
  @Provides
  @PerApp
  internal fun dateTimeProvider(): DateTimeProvider {
    return RealDateTimeProvider()
  }

  @JvmStatic
  @Provides
  @PerApp
  internal fun timeProvider(): TimeProvider {
    return RealTimeProvider.INSTANCE
  }

  @JvmStatic
  @Provides
  @PerApp
  fun schedulers(): AppSchedulers {
    return AppSchedulers(
      AndroidSchedulers.mainThread(),
      Schedulers.io(), Schedulers.computation()
    )
  }

  @JvmStatic
  @Provides
  @IntoSet
  internal fun setupLoggingOnCreate(setupLogging: SetupLogging): OnAppCreate {
    return setupLogging
  }

  @JvmStatic
  @Provides
  @IntoSet
  fun reportAppCreateEvent(eventAnalytics: EventAnalytics): OnAppCreate {
    return object : OnAppCreate {
      override fun onCreate(app: Application) {
        val createEvent = AnalyticsEvent.create("app_create")
        eventAnalytics.report(createEvent)
      }
    }
  }
}
