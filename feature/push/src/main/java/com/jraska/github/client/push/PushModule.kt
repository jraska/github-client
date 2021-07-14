package com.jraska.github.client.push

import android.app.NotificationManager
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.jraska.github.client.core.android.OnAppCreate
import com.jraska.github.client.core.android.ServiceModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import dagger.multibindings.StringKey
import javax.inject.Singleton

@Module(includes = [PushModule.Declarations::class])
object PushModule {

  @Provides
  @Singleton
  internal fun firebaseDatabase(): FirebaseDatabase {
    return FirebaseDatabase.getInstance()
  }

  @Provides
  internal fun notificationManager(context: Context): NotificationManager {
    return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  }

  @Module
  abstract class Declarations {

    @Binds
    @IntoSet
    internal abstract fun bindObserverSetup(observerSetup: PushIntentObserver.CallbacksSetup): OnAppCreate

    @Binds
    @IntoSet
    internal abstract fun setupNotificationsOnCreate(notificationSetup: NotificationSetup): OnAppCreate

    @Binds
    @IntoMap
    @ClassKey(PushHandleModel::class)
    internal abstract fun bindServiceModel(pushHandleModel: PushHandleModel): ServiceModel

    @Binds
    internal abstract fun bindPushModel(pushHandleModel: PushHandleModelImpl): PushHandleModel

    @Binds
    @IntoMap
    @StringKey("refresh_config")
    internal abstract fun refreshConfigCommand(command: RefreshConfigCommand): PushActionCommand

    @Binds
    @IntoMap
    @StringKey("set_config_as_property")
    internal abstract fun configAsPropertyCommand(command: ConfigAsPropertyCommand): PushActionCommand

    @Binds
    @IntoMap
    @StringKey("set_analytics_property")
    internal abstract fun setAnalyticsProperty(command: SetAnalyticsPropertyCommand): PushActionCommand 

    @Binds
    @IntoMap
    @StringKey("notification")
    internal abstract fun notificationCommand(command: ShowNotificationCommand): PushActionCommand 

    @Binds
    @IntoMap
    @StringKey("launch_deep_link")
    internal abstract fun deepLinkCommand(command: LaunchDeepLinkCommand): PushActionCommand

    @Binds
    @IntoMap
    @StringKey("press_back")
    internal abstract fun pressBackCommand(command: PressBackButtonCommand): PushActionCommand

    @Binds
    @IntoMap
    @StringKey("send_push_token")
    internal abstract fun resendPushToken(command: SendPushTokenCommand): PushActionCommand
  }
}
