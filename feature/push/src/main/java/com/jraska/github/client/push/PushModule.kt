package com.jraska.github.client.push

import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.core.android.ServiceModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import dagger.multibindings.StringKey

@Module
object PushModule {

  @Provides
  @IntoSet
  internal fun bindObserverSetup(observerSetup: PushIntentObserver.CallbacksSetup): OnAppCreate {
    return observerSetup
  }

  @Provides
  @IntoSet
  internal fun setupNotificationsOnCreate(notificationSetup: NotificationSetup): OnAppCreate {
    return notificationSetup
  }

  @Provides
  @IntoMap
  @ClassKey(PushHandleModel::class)
  internal fun bindServiceModel(pushHandleModel: PushHandleModel): ServiceModel {
    return pushHandleModel
  }

  @Provides
  @IntoMap
  @StringKey("refresh_config")
  internal fun refreshConfigCommand(command: RefreshConfigCommand): PushActionCommand {
    return command
  }

  @Provides
  @IntoMap
  @StringKey("set_config_as_property")
  internal fun configAsPropertyCommand(command: ConfigAsPropertyCommand): PushActionCommand {
    return command
  }

  @Provides
  @IntoMap
  @StringKey("set_analytics_property")
  internal fun setAnalyticsProperty(command: SetAnalyticsPropertyCommand): PushActionCommand {
    return command
  }

  @Provides
  @IntoMap
  @StringKey("notification")
  internal fun notificationCommand(command: ShowNotificationCommand): PushActionCommand {
    return command
  }

  @Provides
  @IntoMap
  @StringKey("launch_deep_link")
  internal fun deepLinkCommand(command: LaunchDeepLinkCommand): PushActionCommand {
    return command
  }
}
