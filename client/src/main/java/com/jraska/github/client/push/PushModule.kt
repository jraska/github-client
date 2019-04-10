package com.jraska.github.client.push

import android.app.NotificationManager
import android.content.Context
import com.jraska.github.client.Config
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.analytics.AnalyticsProperty
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import dagger.multibindings.StringKey

@Module
object PushModule {
  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("refresh_config")
  fun refreshConfigCommand(config: Config): PushActionCommand {
    return RefreshConfigCommand(config)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("set_config_as_property")
  fun configAsPropertyCommand(config: Config, analyticsProperty: AnalyticsProperty): PushActionCommand {
    return ConfigAsPropertyCommand(config, analyticsProperty)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("set_analytics_property")
  fun setAnalyticsProperty(analyticsProperty: AnalyticsProperty): PushActionCommand {
    return SetAnalyticsPropertyPushCommand(analyticsProperty)
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("notification")
  fun notificationCommand(context: Context, notificationManager: NotificationManager): PushActionCommand {
    return ShowNotificationPushCommand(context, notificationManager)
  }

  @JvmStatic
  @Provides
  @IntoSet
  fun bindObserverSetup(observerSetup: PushIntentObserver.CallbacksSetup): OnAppCreate {
    return observerSetup
  }
}
