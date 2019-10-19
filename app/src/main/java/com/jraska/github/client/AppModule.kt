package com.jraska.github.client

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.facebook.drawee.backends.pipeline.Fresco
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.core.android.ServiceModel
import com.jraska.github.client.core.android.ServiceModelFactory
import com.jraska.github.client.core.android.TopActivityProvider
import com.jraska.github.client.core.android.ViewModelFactory
import com.jraska.github.client.core.android.logging.SetupLogging
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.time.DateTimeProvider
import com.jraska.github.client.time.RealDateTimeProvider
import com.jraska.github.client.time.RealTimeProvider
import com.jraska.github.client.time.TimeProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
object AppModule {

  @Provides
  @JvmStatic
  @PerApp internal fun topActivityProvider(): TopActivityProvider {
    return TopActivityProvider()
  }

  @Provides
  @IntoSet
  @JvmStatic
  fun topActivityOnCreate(setup: TopActivityProvider.RegisterCallbacks): OnAppCreate {
    return setup
  }

  @Provides
  @JvmStatic
  @PerApp internal fun provideLayoutInflater(context: Context): LayoutInflater {
    return LayoutInflater.from(context)
  }

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
  @PerApp internal fun dateTimeProvider(): DateTimeProvider {
    return RealDateTimeProvider()
  }

  @JvmStatic
  @Provides
  @PerApp internal fun timeProvider(): TimeProvider {
    return RealTimeProvider.INSTANCE
  }

  @JvmStatic
  @Provides internal fun notificationManager(context: Context): NotificationManager {
    return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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

  @JvmStatic
  @Provides
  @IntoSet
  fun setupFresco(): OnAppCreate {
    return object : OnAppCreate {
      override fun onCreate(app: Application) = Fresco.initialize(app)
    }
  }

  @JvmStatic
  @Provides
  @IntoSet
  fun setupThreeTen(): OnAppCreate {
    return object : OnAppCreate {
      override fun onCreate(app: Application) = AndroidThreeTen.init(app)
    }
  }
}
