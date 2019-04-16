package com.jraska.github.client.push

import android.app.NotificationManager
import android.content.Context
import com.jraska.github.client.Config
import com.jraska.github.client.analytics.AnalyticsProperty
import com.jraska.github.client.core.android.OnAppCreate
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import dagger.multibindings.StringKey

@Module
object PushModule {
  @JvmStatic
  @Provides
  @IntoSet
  internal fun bindObserverSetup(observerSetup: PushIntentObserver.CallbacksSetup): OnAppCreate {
    return observerSetup
  }

  @Provides
  @JvmStatic
  @IntoSet
  internal fun setupNotificationsOnCreate(notificationSetup: NotificationSetup): OnAppCreate {
    return notificationSetup
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("refresh_config")
  internal fun refreshConfigCommand(config: Config): PushActionCommand {
    return RefreshConfigCommand(config)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("set_config_as_property")
  internal fun configAsPropertyCommand(config: Config, analyticsProperty: AnalyticsProperty): PushActionCommand {
    return ConfigAsPropertyCommand(config, analyticsProperty)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("set_analytics_property")
  internal fun setAnalyticsProperty(analyticsProperty: AnalyticsProperty): PushActionCommand {
    return SetAnalyticsPropertyPushCommand(analyticsProperty)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("notification")
  internal fun notificationCommand(context: Context, notificationManager: NotificationManager): PushActionCommand {
    return ShowNotificationPushCommand(context, notificationManager)
  }
}
