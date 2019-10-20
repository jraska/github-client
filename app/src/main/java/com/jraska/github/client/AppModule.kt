package com.jraska.github.client

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.view.LayoutInflater
import com.facebook.drawee.backends.pipeline.Fresco
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jraska.github.client.core.android.OnAppCreate
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
object AppModule {

  @Provides
  @JvmStatic
  @PerApp
  internal fun provideLayoutInflater(context: Context): LayoutInflater {
    return LayoutInflater.from(context)
  }

  @JvmStatic
  @Provides
  internal fun notificationManager(context: Context): NotificationManager {
    return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
