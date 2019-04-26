package com.jraska.github.client.push

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
  internal fun refreshConfigCommand(command: RefreshConfigCommand): PushActionCommand {
    return command
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("set_config_as_property")
  internal fun configAsPropertyCommand(command: ConfigAsPropertyCommand): PushActionCommand {
    return command
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("set_analytics_property")
  internal fun setAnalyticsProperty(command: SetAnalyticsPropertyPushCommand): PushActionCommand {
    return command
  }

  @JvmStatic
  @Provides
  @IntoMap
  @StringKey("notification")
  internal fun notificationCommand(command: ShowNotificationPushCommand): PushActionCommand {
    return command
  }
}
